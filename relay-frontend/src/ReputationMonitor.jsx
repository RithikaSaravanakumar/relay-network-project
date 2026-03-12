import React, { useState } from 'react';
import API from './api';

export default function ReputationMonitor() {
  const [nodeId, setNodeId] = useState('');
  const [chainScore, setChainScore] = useState(null);
  const [calcScore, setCalcScore] = useState(null);

  const fetchChain = async () => {
    try {
      const res = await API.getReputation(nodeId);
      setChainScore(res.data.data);
    } catch (e) {
      setChainScore(null);
    }
  };

  const calculate = async () => {
    try {
      const res = await API.calculateReputation(nodeId);
      setCalcScore(res.data.data);
    } catch (e) {
      setCalcScore(null);
    }
  };

  return (
    <section className="card">
      <div className="card-header"><h2>🧾 Reputation Monitor</h2></div>
      <div className="card-body">
        <div className="input-group">
          <input placeholder="Node ID" value={nodeId} onChange={e=>setNodeId(e.target.value)} className="input-field" />
          <button className="btn" onClick={fetchChain}>Fetch On-Chain</button>
          <button className="btn btn-secondary" onClick={calculate}>Recalculate</button>
        </div>

        <div style={{marginTop:12}}>
          <p><strong>On-chain score:</strong> {chainScore !== null ? chainScore : '—'}</p>
          <p><strong>Calculated score:</strong> {calcScore !== null ? calcScore : '—'}</p>
        </div>
      </div>
    </section>
  );
}
