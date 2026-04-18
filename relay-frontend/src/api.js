import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('relay_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('relay_token');
      localStorage.removeItem('relay_user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// API functions
const registerNode = () => apiClient.post('/node/register');
const getNode = (nodeId) => apiClient.get(`/node/${encodeURIComponent(nodeId)}`);
const checkHealth = () => apiClient.get('/node/health');
const validateToken = () => apiClient.get('/auth/validate');

const getAllNodes = () => apiClient.get('/node/all');
const getRelays = () => apiClient.get('/relay/list');
const getMessages = () => apiClient.get('/messages');
const getNetworkStatus = () => apiClient.get('/network/status');

const registerRelay = (payload) => apiClient.post('/relay/register', payload);
const updateRelayPerformance = (payload) => apiClient.post('/relay/performance', payload);

// const sendMessage = (payload) => apiClient.post('/message/send', payload);
// const sendMessage = (payload) => apiClient.post('/message/send', payload);
// const sendMessage = (payload) =>
//   apiClient.post(`/messages/send?sourceNode=${payload.sourceNode}&destinationNode=${payload.destinationNode}&content=${payload.content}`);
const sendMessage = (payload) => apiClient.post('/messages/send', payload);
const calculateReputation = (nodeId) => apiClient.get(`/reputation/calculate?nodeId=${encodeURIComponent(nodeId)}`);
const getReputation = (nodeId) => apiClient.get(`/reputation/get?nodeId=${encodeURIComponent(nodeId)}`);

// Named exports for direct import and a default export for backward compatibility
export { registerNode, getNode, checkHealth, getAllNodes, getRelays, getMessages, getNetworkStatus, sendMessage, registerRelay, updateRelayPerformance, calculateReputation, getReputation, validateToken, apiClient };

export default {
  registerNode,
  getNode,
  checkHealth,
  getAllNodes,
  getRelays,
  getMessages,
  getNetworkStatus,
  sendMessage,
  registerRelay,
  updateRelayPerformance,
  calculateReputation,
  getReputation,
  validateToken,
  apiClient,
};
