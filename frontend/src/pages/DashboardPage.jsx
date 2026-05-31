import { Box, Button, Card, CardContent, Grid, Stack, Typography } from '@mui/material';
import { Link } from 'react-router-dom';

const sections = [
  { title: 'Books', text: 'Manage book catalog, prices, stock and categories.', path: '/books' },
  { title: 'Categories', text: 'Create and organize book categories.', path: '/categories' },
  { title: 'Orders', text: 'Create orders and update order status.', path: '/orders' },
  { title: 'Reviews', text: 'Add and manage book reviews.', path: '/reviews' }
];

export default function DashboardPage() {
  return (
    <Box>
      <Box className="hero-section">
        <Typography variant="overline" className="hero-label">Spring Boot + React</Typography>
        <Typography variant="h3" className="hero-title">Book Store Dashboard</Typography>
        <Typography className="hero-text">
          A simple full-stack book e-commerce app with JWT authentication, REST APIs, PostgreSQL and React UI.
        </Typography>
        <Stack direction="row" spacing={2} sx={{ mt: 3 }}>
          <Button variant="contained" component={Link} to="/books">Browse Books</Button>
          <Button variant="outlined" component={Link} to="/login">Login</Button>
        </Stack>
      </Box>

      <Grid container spacing={3} sx={{ mt: 1 }}>
        {sections.map((section) => (
          <Grid item xs={12} md={6} key={section.title}>
            <Card className="soft-card">
              <CardContent>
                <Typography variant="h6">{section.title}</Typography>
                <Typography color="text.secondary" sx={{ mt: 1, minHeight: 48 }}>{section.text}</Typography>
                <Button sx={{ mt: 2 }} component={Link} to={section.path}>Open</Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
