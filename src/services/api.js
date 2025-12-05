import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add user ID to headers for authenticated requests
api.interceptors.request.use((config) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  if (user.id) {
    config.headers['X-User-Id'] = user.id;
  }
  return config;
});

export const userAPI = {
  register: (username, password) =>
    api.post('/users/register', { username, password }),
  
  login: (username, password) =>
    api.post('/users/login', { username, password }),
};

export const postAPI = {
  createPost: (content) =>
    api.post('/posts', { content }),
  
  getFeed: () =>
    api.get('/posts/feed'),
};

export const likeAPI = {
  toggleLike: (postId) =>
    api.post(`/likes/toggle/${postId}`),
};

export default api;







