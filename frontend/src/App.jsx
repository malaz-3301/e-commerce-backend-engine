import { useState } from 'react';
import { Link, Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import { AppBar, Box, Button, Container, Stack, Toolbar, Typography } from '@mui/material';
import LoginPage from './pages/LoginPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import BooksPage from './pages/BooksPage.jsx';
import CategoriesPage from './pages/CategoriesPage.jsx';
import OrdersPage from './pages/OrdersPage.jsx';
import ReviewsPage from './pages/ReviewsPage.jsx';
import { clearToken, getRole, getToken, isAdmin } from './api.js';

export default function App() {
  const [token, setTokenState] = useState(getToken());
  const [role, setRole] = useState(getRole());
  const navigate = useNavigate();
  const location = useLocation();
  const admin = isAdmin();

  const links = [
    { label: 'Home', path: '/' },
    ...(admin ? [
      { label: 'Categories', path: '/categories' },
      { label: 'Orders', path: '/orders' },
      { label: 'Reviews', path: '/reviews' }
    ] : [])
  ];

  function handleAuthChange() {
    setTokenState(getToken());
    setRole(getRole());
  }

  function logout() {
    clearToken();
    setTokenState(null);
    setRole(null);
    navigate('/');
  }

  return (
    <Box className="app-shell">
      <AppBar position="sticky" elevation={0} className="topbar">
        <Toolbar className="toolbar">
          <Box className="brand" component={Link} to="/">
            <span className="brand-icon">B</span>
            <Typography variant="h6">Book Store</Typography>
          </Box>

          <Stack direction="row" spacing={1} className="nav-links">
            {links.map((link) => (
              <Button
                key={link.path}
                component={Link}
                to={link.path}
                className={location.pathname === link.path ? 'nav-button active' : 'nav-button'}
              >
                {link.label}
              </Button>
            ))}
          </Stack>

          <Stack direction="row" spacing={1} alignItems="center">
            {role && <Typography className="role-pill">{role}</Typography>}
            {!token && <Button variant="outlined" color="inherit" component={Link} to="/login">Login</Button>}
            {!token && <Button variant="contained" component={Link} to="/register">Register</Button>}
            {token && <Button variant="outlined" color="inherit" onClick={logout}>Logout</Button>}
          </Stack>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Routes>
          <Route path="/" element={<BooksPage />} />
          <Route path="/login" element={<LoginPage onAuthChange={handleAuthChange} />} />
          <Route path="/register" element={<RegisterPage onAuthChange={handleAuthChange} />} />
          <Route path="/books" element={<BooksPage />} />
          {admin && <Route path="/categories" element={<CategoriesPage />} />}
          {admin && <Route path="/orders" element={<OrdersPage />} />}
          {admin && <Route path="/reviews" element={<ReviewsPage />} />}
          <Route path="*" element={<BooksPage />} />
        </Routes>
      </Container>
    </Box>
  );
}
