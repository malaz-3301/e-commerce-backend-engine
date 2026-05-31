import { useEffect, useState } from 'react';
import { Alert, Box, Button, Card, CardContent, Grid, Stack, TextField, Typography } from '@mui/material';
import { apiRequest } from '../api.js';

export default function CategoriesPage() {
  const [categories, setCategories] = useState([]);
  const [name, setName] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    loadCategories();
  }, []);

  async function loadCategories() {
    try {
      setCategories(await apiRequest('/categories'));
    } catch (err) {
      setError(err.message);
    }
  }

  async function createCategory(event) {
    event.preventDefault();
    setError('');
    try {
      await apiRequest('/categories', {
        method: 'POST',
        body: JSON.stringify({ name })
      });
      setName('');
      loadCategories();
    } catch (err) {
      setError(err.message);
    }
  }

  async function deleteCategory(id) {
    try {
      await apiRequest(`/categories/${id}`, { method: 'DELETE' });
      loadCategories();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <Box>
      <Box className="section-header">
        <Box>
          <Typography variant="h4" className="section-title">Categories</Typography>
          <Typography className="section-subtitle">Organize books into simple store sections.</Typography>
        </Box>
        <span className="badge">{categories.length} categories</span>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Card className="data-card" sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6" fontWeight={800}>New category</Typography>
          <Box component="form" onSubmit={createCategory} className="inline-form" sx={{ mt: 2 }}>
            <TextField fullWidth label="Category name" value={name} onChange={(e) => setName(e.target.value)} required />
            <Button type="submit" variant="contained" size="large">Save</Button>
          </Box>
        </CardContent>
      </Card>

      <Grid container spacing={2}>
        {categories.map((category) => (
          <Grid item xs={12} md={4} key={category.id}>
            <Card className="item-card">
              <CardContent>
                <Stack spacing={1.5}>
                  <Typography variant="overline" color="primary">Category #{category.id}</Typography>
                  <Typography variant="h6" fontWeight={800}>{category.name}</Typography>
                  <Typography color="text.secondary">Used to group related books in the store.</Typography>
                  <Button color="error" onClick={() => deleteCategory(category.id)}>Delete</Button>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
