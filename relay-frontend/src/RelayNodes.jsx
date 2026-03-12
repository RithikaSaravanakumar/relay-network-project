import React, { useEffect, useState } from 'react';
import API from './api';

export default function RelayNodes() {
  const [relays, setRelays] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => { load(); }, []);

  const load = async () => {
    setLoading(true);
    try {
      const res = await API.getRelays();
      setRelays(res.data.data || []);
    } catch (e) {
      setRelays([]);
    } finally { setLoading(false); }
  };

  return (
    <section className="card">
      <div className="card-header"><h2>🔁 Relay Nodes</h2></div>
      <div className="card-body">
        {loading && <p>Loading relays...</p>}
        {!loading && relays.length === 0 && <p>No relays registered</p>}
        {!loading && relays.length > 0 && (
          <table className="nodes-table">
            <thead><tr><th>Relay ID</th><th>Node ID</th><th>PDR</th><th>Latency</th><th>Failure</th><th>Status</th></tr></thead>
            <tbody>
              {relays.map(r => (
                <tr key={r.relayId}>
                  <td><code>{r.relayId}</code></td>
                  <td><code>{r.nodeId?.substring(0, 18)}...</code></td>
                  <td>{r.packetDeliveryRatio}</td>
                  <td>{r.latency}</td>
                  <td>{r.failureRate}</td>
                  <td>{r.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </section>
  );
}
