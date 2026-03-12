import React, { useState } from 'react';
import API from './api';

export default function RelayPerformance() {
  const [relayId, setRelayId] = useState('');
  const [latency, setLatency] = useState('');
  const [pdr, setPdr] = useState('');
  const [failure, setFailure] = useState('');
  const [result, setResult] = useState(null);

  const submit = async () => {
    setResult(null);
    try {
      const payload = {
        relayId,
        latency: latency ? parseFloat(latency) : null,
        packetDeliveryRatio: pdr ? parseFloat(pdr) : null,
        failureRate: failure ? parseFloat(failure) : null,
      };
      const res = await API.updateRelayPerformance(payload);
      setResult({ success: true, data: res.data.data });
    } catch (e) {
      setResult({ success: false, message: e.response?.data?.message || 'Failed' });
    }
  };

  return (
    <section className="card">
      <div className="card-header"><h2>⚙️ Relay Performance</h2></div>
      <div className="card-body">
        <div className="input-group">
          <input placeholder="Relay ID" value={relayId} onChange={e=>setRelayId(e.target.value)} className="input-field" />
          <input placeholder="Latency (ms)" value={latency} onChange={e=>setLatency(e.target.value)} className="input-field" />
          <input placeholder="Packet Delivery Ratio (0-1)" value={pdr} onChange={e=>setPdr(e.target.value)} className="input-field" />
          <input placeholder="Failure Rate (0-1)" value={failure} onChange={e=>setFailure(e.target.value)} className="input-field" />
        </div>
        <button className="btn btn-primary" onClick={submit}>Update Performance</button>

        {result && result.success && (
          <div className="result-container success">
            <pre>{JSON.stringify(result.data, null, 2)}</pre>
          </div>
        )}
        {result && !result.success && (
          <div className="result-container error">
            <p>{result.message}</p>
          </div>
        )}
      </div>
    </section>
  );
}
