import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// API functions
const registerNode = () => apiClient.post('/node/register');
const getNode = (nodeId) => apiClient.get(`/node/${encodeURIComponent(nodeId)}`);
const checkHealth = () => apiClient.get('/node/health');

const getAllNodes = () => apiClient.get('/network/nodes');
const getRelays = () => apiClient.get('/relay/list');
const getNetworkStatus = () => apiClient.get('/network/status');

const registerRelay = (payload) => apiClient.post('/relay/register', payload);
const updateRelayPerformance = (payload) => apiClient.post('/relay/performance', payload);

// const sendMessage = (payload) => apiClient.post('/message/send', payload);
// const sendMessage = (payload) => apiClient.post('/message/send', payload);
// const sendMessage = (payload) =>
//   apiClient.post(`/messages/send?sourceNode=${payload.sourceNode}&destinationNode=${payload.destinationNode}&content=${payload.content}`);
const sendMessage = ({ sourceNode, destinationNode, content }) =>
  apiClient.post(
    `/messages/send?sourceNode=${encodeURIComponent(sourceNode)}&destinationNode=${encodeURIComponent(destinationNode)}&content=${encodeURIComponent(content)}`
  );
const calculateReputation = (nodeId) => apiClient.get(`/reputation/calculate?nodeId=${encodeURIComponent(nodeId)}`);
const getReputation = (nodeId) => apiClient.get(`/reputation/get?nodeId=${encodeURIComponent(nodeId)}`);

// Named exports for direct import and a default export for backward compatibility
export { registerNode, getNode, checkHealth, getAllNodes, getRelays, getNetworkStatus, sendMessage, registerRelay, updateRelayPerformance, calculateReputation, getReputation, apiClient };

export default {
  registerNode,
  getNode,
  checkHealth,
  getAllNodes,
  getRelays,
  getNetworkStatus,
  sendMessage,
  registerRelay,
  updateRelayPerformance,
  calculateReputation,
  getReputation,
  apiClient,
};
