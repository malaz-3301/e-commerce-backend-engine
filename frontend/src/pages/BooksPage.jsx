import { useEffect, useState } from 'react';
import { Alert, Box, Button, Card, CardContent, Divider, Grid, Stack, TextField, Typography } from '@mui/material';
import { apiRequest, isAdmin } from '../api.js';

export default function BooksPage() {
  const [books, setBooks] = useState([]);
  const [form, setForm] = useState({ title: '', author: '', description: '', price: '', stockQuantity: '', categoryId: '' });
  const [error, setError] = useState('');
  const admin = isAdmin();

  useEffect(() => {
    loadBooks();
  }, []);

  async function loadBooks() {
    try {
      setBooks(await apiRequest('/books'));
    } catch (err) {
      setError(err.message);
    }
  }

  function updateField(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  async function createBook(event) {
    event.preventDefault();
    setError('');
    try {
      await apiRequest('/books', {
        method: 'POST',
        body: JSON.stringify({
          ...form,
          price: Number(form.price),
          stockQuantity: Number(form.stockQuantity),
          categoryId: Number(form.categoryId)
        })
      });
      setForm({ title: '', author: '', description: '', price: '', stockQuantity: '', categoryId: '' });
      loadBooks();
    } catch (err) {
      setError(err.message);
    }
  }

  async function deleteBook(id) {
    try {
      await apiRequest(`/books/${id}`, { method: 'DELETE' });
      loadBooks();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <Box>
      <Box className="home-hero">
        <Typography variant="overline" className="hero-label">Online Book Store</Typography>
        <Typography variant="h3" className="hero-title">Browse our books collection</Typography>
        <Typography className="hero-text">
          Discover available books, prices, authors and stock. Login only when you need to manage or order.
        </Typography>
      </Box>

      <Box className="section-header" sx={{ mt: 4 }}>
        <Box>
          <Typography variant="h4" className="section-title">Books</Typography>
          <Typography className="section-subtitle">
            {admin ? 'Admin mode: you can create and delete books.' : 'Public catalog: available for all visitors.'}
          </Typography>
        </Box>
        <span className="badge">{books.length} books</span>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      {admin && (
        <Card className="data-card" sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6">Add a new book</Typography>
            <Typography color="text.secondary" sx={{ mb: 2 }}>Fill basic book information and link it to a category id.</Typography>
            <Box component="form" onSubmit={createBook} className="form-grid two-columns">
              <TextField label="Title" name="title" value={form.title} onChange={updateField} required />
              <TextField label="Author" name="author" value={form.author} onChange={updateField} required />
              <TextField label="Price" name="price" type="number" value={form.price} onChange={updateField} required />
              <TextField label="Stock" name="stockQuantity" type="number" value={form.stockQuantity} onChange={updateField} required />
              <TextField label="Category ID" name="categoryId" type="number" value={form.categoryId} onChange={updateField} required />
              <TextField label="Description" name="description" value={form.description} onChange={updateField} />
              <Button type="submit" variant="contained" size="large">Save Book</Button>
            </Box>
          </CardContent>
        </Card>
      )}

      <Grid container spacing={3}>
        {books.map((book) => (
          <Grid item xs={12} md={4} key={book.id}>
            <Card className="item-card">
              <CardContent>
                <Stack spacing={1.2}>
                  <Typography variant="overline" color="primary">Book #{book.id}</Typography>
                  <Typography variant="h6" sx={{ fontWeight: 800 }}>{book.title}</Typography>
                  <Typography color="text.secondary">{book.author}</Typography>
                  <Divider />
                  <Stack direction="row" justifyContent="space-between">
                    <Typography>Price</Typography>
                    <Typography fontWeight={700}>{book.price}</Typography>
                  </Stack>
                  <Stack direction="row" justifyContent="space-between">
                    <Typography>Stock</Typography>
                    <Typography fontWeight={700}>{book.stockQuantity}</Typography>
                  </Stack>
                  {admin && <Button color="error" onClick={() => deleteBook(book.id)}>Delete</Button>}
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
