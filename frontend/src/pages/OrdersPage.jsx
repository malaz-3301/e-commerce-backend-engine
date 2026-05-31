import { useEffect, useState } from 'react';
import { Alert, Box, Button, Card, CardContent, Chip, Grid, Stack, TextField, Typography } from '@mui/material';
import { apiRequest } from '../api.js';

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [form, setForm] = useState({ userId: '', bookId: '', quantity: 1 });
  const [error, setError] = useState('');

  useEffect(() => {
    loadOrders();
  }, []);

  async function loadOrders() {
    try {
      setOrders(await apiRequest('/orders'));
    } catch (err) {
      setError(err.message);
    }
  }

  function updateField(event) {
    setForm({ ...form, [event.target.name]: event.target.value });
  }

  async function createOrder(event) {
    event.preventDefault();
    setError('');
    try {
      await apiRequest('/orders', {
        method: 'POST',
        body: JSON.stringify({
          userId: Number(form.userId),
          items: [{ bookId: Number(form.bookId), quantity: Number(form.quantity) }]
        })
      });
      setForm({ userId: '', bookId: '', quantity: 1 });
      loadOrders();
    } catch (err) {
      setError(err.message);
    }
  }

  async function confirmOrder(id) {
    try {
      await apiRequest(`/orders/${id}/status?status=CONFIRMED`, { method: 'PUT' });
      loadOrders();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <Box>
      <Box className="section-header">
        <Box>
          <Typography variant="h4" className="section-title">Orders</Typography>
          <Typography className="section-subtitle">Create customer orders and update their status.</Typography>
        </Box>
        <span className="badge">{orders.length} orders</span>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Card className="data-card" sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6" fontWeight={800}>Create order</Typography>
          <Box component="form" onSubmit={createOrder} className="form-grid two-columns">
            <TextField label="User ID" name="userId" type="number" value={form.userId} onChange={updateField} required />
            <TextField label="Book ID" name="bookId" type="number" value={form.bookId} onChange={updateField} required />
            <TextField label="Quantity" name="quantity" type="number" value={form.quantity} onChange={updateField} required />
            <Button type="submit" variant="contained" size="large">Create Order</Button>
          </Box>
        </CardContent>
      </Card>

      <Grid container spacing={2}>
        {orders.map((order) => (
          <Grid item xs={12} md={6} key={order.id}>
            <Card className="item-card">
              <CardContent>
                <Stack spacing={1.5}>
                  <Stack direction="row" justifyContent="space-between" alignItems="center">
                    <Typography variant="h6" fontWeight={800}>Order #{order.id}</Typography>
                    <Chip label={order.status} color={order.status === 'CONFIRMED' ? 'success' : 'warning'} size="small" />
                  </Stack>
                  <Typography color="text.secondary">Total amount</Typography>
                  <Typography variant="h5" fontWeight={800}>{order.totalAmount}</Typography>
                  <Button variant="outlined" onClick={() => confirmOrder(order.id)}>Mark as confirmed</Button>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
