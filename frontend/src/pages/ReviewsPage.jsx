import { useEffect, useState } from 'react';
import { Alert, Box, Button, Card, CardContent, Grid, Rating, Stack, TextField, Typography } from '@mui/material';
import { apiRequest } from '../api.js';

export default function ReviewsPage() {
  const [reviews, setReviews] = useState([]);
  const [form, setForm] = useState({ userId: '', bookId: '', rating: 5, comment: '' });
  const [error, setError] = useState('');

  useEffect(() => {
    loadReviews();
  }, []);

  async function loadReviews() {
    try {
      setReviews(await apiRequest('/reviews'));
    } catch (err) {
      setError(err.message);
    }
  }

  function updateField(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  async function createReview(event) {
    event.preventDefault();
    setError('');
    try {
      await apiRequest('/reviews', {
        method: 'POST',
        body: JSON.stringify({
          userId: Number(form.userId),
          bookId: Number(form.bookId),
          rating: Number(form.rating),
          comment: form.comment
        })
      });
      setForm({ userId: '', bookId: '', rating: 5, comment: '' });
      loadReviews();
    } catch (err) {
      setError(err.message);
    }
  }

  async function deleteReview(id) {
    try {
      await apiRequest(`/reviews/${id}`, { method: 'DELETE' });
      loadReviews();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <Box>
      <Box className="section-header">
        <Box>
          <Typography variant="h4" className="section-title">Reviews</Typography>
          <Typography className="section-subtitle">Collect and manage feedback about books.</Typography>
        </Box>
        <span className="badge">{reviews.length} reviews</span>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Card className="data-card" sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6" fontWeight={800}>Add review</Typography>
          <Box component="form" onSubmit={createReview} className="form-grid two-columns">
            <TextField label="User ID" name="userId" type="number" value={form.userId} onChange={updateField} required />
            <TextField label="Book ID" name="bookId" type="number" value={form.bookId} onChange={updateField} required />
            <TextField label="Rating" name="rating" type="number" value={form.rating} onChange={updateField} required />
            <TextField label="Comment" name="comment" value={form.comment} onChange={updateField} required />
            <Button type="submit" variant="contained" size="large">Add Review</Button>
          </Box>
        </CardContent>
      </Card>

      <Grid container spacing={2}>
        {reviews.map((review) => (
          <Grid item xs={12} md={6} key={review.id}>
            <Card className="item-card">
              <CardContent>
                <Stack spacing={1.5}>
                  <Typography variant="overline" color="primary">Review #{review.id}</Typography>
                  <Rating value={review.rating} readOnly />
                  <Typography color="text.secondary">{review.comment}</Typography>
                  <Button color="error" onClick={() => deleteReview(review.id)}>Delete</Button>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
