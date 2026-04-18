import React, { useState } from 'react';
import axios from 'axios';

export default function RelayCreation({ onRelayCreated }) {
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleCreate = async () => {
    setLoading(true);
    setResult(null);
    try {
      const token = localStorage.getItem('relay_token');
      const res = await axios.post('http://localhost:8080/api/relay/create', {}, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setResult({ success: true, id: res.data.relayId });
      if(onRelayCreated) onRelayCreated(res.data);
    } catch (err) {
      setResult({ success: false, error: 'Failed to create relay' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="glass rounded-2xl p-6 border border-white/5 relative overflow-hidden group">
      <div className="absolute top-0 right-0 p-4 opacity-10 text-6xl">🛰️</div>
      <h2 className="text-lg font-bold flex items-center gap-2 mb-4">
        ⚙️ <span className="bg-gradient-to-r from-blue-400 to-indigo-400 bg-clip-text text-transparent">Relay Network Admin</span>
      </h2>

      <p className="text-sm text-gray-400 mb-6 relative z-10">
        Push a new Relay Node dynamically onto the simulated P2P network to test scaling routing capacities.
      </p>

      <div className="space-y-4 relative z-10">
        <button 
          onClick={handleCreate} 
          disabled={loading}
          className="w-full py-3 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-500 hover:to-indigo-500 rounded-xl font-bold transition-all shadow-lg shadow-blue-500/25 disabled:opacity-50"
        >
          {loading ? 'Booting Relay Node...' : '➕ Spawn New Relay Node'}
        </button>

        {result && result.success && (
          <div className="p-3 bg-emerald-500/10 border border-emerald-500/20 rounded-lg text-emerald-400 text-sm">
            ✅ Relay Spawend: <span className="font-mono">{result.id}</span>
          </div>
        )}

        {result && !result.success && (
          <div className="p-3 bg-red-500/10 border border-red-500/20 rounded-lg text-red-400 text-sm">
            ❌ {result.error}
          </div>
        )}
      </div>
    </div>
  );
}
