import React, { useEffect, useState } from 'react';
import NodeAPI from './api';

export default function RelaySelector() {
  const [relays, setRelays] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadRelays();
  }, []);

  const loadRelays = async () => {
    setLoading(true);
    try {
      const res = await NodeAPI.getRelays();
      setRelays(res.data.data || []);
    } catch (e) {
      setRelays([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="card">
      <div className="card-header">
        <h2>🧭 Relay Selection</h2>
      </div>
      <div className="card-body">
        {loading && <p>Loading relays...</p>}
        {!loading && relays.length === 0 && <p>No relay nodes available</p>}
        {!loading && relays.length > 0 && (
          <div>
            <h3>Best Relay: {relays[0].nodeId ? relays[0].nodeId.substring(0, 24) + '...' : 'N/A'}</h3>
            <ul>
              {relays.slice(0, 5).map(r => (
                <li key={r.nodeId}>{r.nodeId.substring(0, 16)}... — Trust: {r.trustScore}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </section>
  );
}
