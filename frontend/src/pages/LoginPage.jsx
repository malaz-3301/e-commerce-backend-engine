import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Alert, Box, Button, Card, CardContent, Divider, Stack, TextField, Typography } from '@mui/material';
import { apiRequest, setToken } from '../api.js';

export default function LoginPage({ onAuthChange }) {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  function updateField(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      const data = await apiRequest('/auth/login', {
        method: 'POST',
        body: JSON.stringify(form)
      });
      setToken(data.token);
      onAuthChange();
      navigate('/books');
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <Card className="page-card">
      <CardContent sx={{ p: 4 }}>
        <Stack spacing={1} sx={{ mb: 3 }}>
          <Typography variant="overline" color="primary" fontWeight={800}>Welcome back</Typography>
          <Typography variant="h4" fontWeight={800}>Login</Typography>
          <Typography color="text.secondary">Use your account to manage books, orders and reviews.</Typography>
        </Stack>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        <Box component="form" onSubmit={submit} className="form-grid">
          <TextField label="Username" name="username" value={form.username} onChange={updateField} required />
          <TextField label="Password" name="password" type="password" value={form.password} onChange={updateField} required />
          <Button type="submit" variant="contained" size="large">Login</Button>
        </Box>
        <Divider sx={{ my: 3 }} />
        <Typography color="text.secondary">
          New user? <Button component={Link} to="/register">Create account</Button>
        </Typography>
      </CardContent>
    </Card>
  );
}
