import React, { useEffect, useState } from 'react';
import NodeAPI from './api';

export default function TrustScoreMonitor() {
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => { load(); }, []);

  const load = async () => {
    setLoading(true);
    try {
      const res = await NodeAPI.getNetworkStatus();
      setStatus(res.data.data);
    } catch (e) {
      setStatus(null);
    } finally { setLoading(false); }
  };

  return (
    <section className="card">
      <div className="card-header"><h2>📊 Trust Score Monitor</h2></div>
      <div className="card-body">
        {loading && <p>Loading status...</p>}
        {status && (
          <div>
            <p><strong>Total Nodes:</strong> {status.totalNodes}</p>
            <p><strong>Malicious Nodes:</strong> {status.maliciousNodes}</p>
            <p><strong>Average Trust:</strong> {Math.round(status.averageTrustScore)}</p>
          </div>
        )}
      </div>
    </section>
  );
}
