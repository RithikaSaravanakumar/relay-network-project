import React, { useEffect, useState } from 'react';
import NodeAPI from './api';

export default function NetworkNodes() {
  const [nodes, setNodes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
  fetchNodes();

  const interval = setInterval(fetchNodes, 3000);

  return () => clearInterval(interval);
}, []);

  const fetchNodes = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await NodeAPI.getAllNodes();
      setNodes(res.data.data || []);
    } catch (e) {
      setError('Failed to load nodes');
    } finally {
      setLoading(false);
    }
  };

  const formatId = (id) => {
    if (!id) return '';
    return id.length > 30 ? `${id.substring(0, 15)}...${id.substring(id.length - 10)}` : id;
  };

  return (
    <section className="card">
      <div className="card-header">
        <h2>📡 Network Nodes</h2>
      </div>
      <div className="card-body">
        {loading && <p>Loading nodes...</p>}
        {error && <p className="error">{error}</p>}
        {!loading && !error && (
          <table className="nodes-table">
            <thead>
              <tr>
                <th>Node ID</th>
                <th>Trust Score</th>
                <th>Malicious</th>
              </tr>
            </thead>
            <tbody>
              {nodes.map((n) => (
                <tr key={n.nodeId}>
                  <td><code>{formatId(n.nodeId)}</code></td>
                  <td>{n.trustScore}</td>
                  <td>{n.malicious ? 'Yes' : 'No'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </section>
  );
}
