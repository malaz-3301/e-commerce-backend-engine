const API_BASE_URL = '/api';

export function getToken() {
  return localStorage.getItem('token');
}

export function setToken(token) {
  localStorage.setItem('token', token);
}

export function clearToken() {
  localStorage.removeItem('token');
}

export function getRole() {
  const token = getToken();
  if (!token) {
    return null;
  }

  try {
    const payload = atob(token.split('.')[0].replace(/-/g, '+').replace(/_/g, '/'));
    return payload.split(':')[1] || null;
  } catch {
    return null;
  }
}

export function isAdmin() {
  return getRole() === 'ADMIN';
}

export async function apiRequest(path, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers
  });

  if (!response.ok) {
    let message = 'Request failed';
    try {
      const error = await response.json();
      message = error.message || JSON.stringify(error);
    } catch {
      message = response.statusText;
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}
