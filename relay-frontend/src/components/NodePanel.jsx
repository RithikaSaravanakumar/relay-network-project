import React, { useState, useEffect } from 'react';
import API from '../api';

export default function NodePanel() {
  const [nodes, setNodes] = useState([]);
  const [loading, setLoading] = useState(false);
  const userStr = localStorage.getItem('relay_user');
  const userObj = userStr ? JSON.parse(userStr) : null;

  const fetchNodes = async () => {
    try {
      const res = await API.getAllNodes();
      setNodes(res.data?.data || []);
    } catch (e) {
      console.error('Failed to fetch nodes', e);
    }
  };

  useEffect(() => {
    fetchNodes();
  }, []);

  const handleCreateNode = async () => {
    setLoading(true);
    try {
      await API.registerNode();
      await fetchNodes();
    } catch (e) {
      console.error('Failed to register node', e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="glass rounded-2xl p-6 relative overflow-hidden group border border-white/5">
      <div className="absolute top-0 right-0 p-4 opacity-5 text-8xl">🌐</div>
      <div className="flex justify-between items-center mb-6 relative z-10">
        <h2 className="text-lg font-bold flex items-center gap-2">
          🔑 <span className="bg-gradient-to-r from-emerald-400 to-teal-400 bg-clip-text text-transparent">Network Nodes</span>
        </h2>
        <button 
          onClick={handleCreateNode} 
          disabled={loading}
          className="px-4 py-2 bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white text-sm font-bold rounded-lg shadow-lg disabled:opacity-50 transition-all">
          {loading ? 'Creating...' : '➕ Create New Node'}
        </button>
      </div>

      <div className="space-y-3 relative z-10 max-h-96 overflow-y-auto pr-2 custom-scrollbar">
        {nodes.length === 0 ? (
           <div className="text-gray-400 text-sm py-4 text-center">No nodes registered on the network yet.</div>
        ) : (
          nodes.map((node, i) => (
            <div key={node.nodeId || i} className={`bg-white/5 p-4 rounded-xl border ${userObj && userObj.nodeId === node.nodeId ? 'border-emerald-500/30 bg-emerald-500/10' : 'border-white/5'}`}>
              <div className="flex justify-between items-center mb-2">
                <label className="text-xs text-gray-500 uppercase font-bold tracking-wider">Node ID</label>
                {userObj && userObj.nodeId === node.nodeId && (
                  <span className="text-[10px] bg-emerald-500/20 text-emerald-400 px-2 py-0.5 rounded uppercase font-bold tracking-wider">My Node</span>
                )}
              </div>
              <div className="text-emerald-400 font-mono text-sm break-all mb-3 relative group">
                {node.nodeId}
              </div>

              <label className="text-xs text-gray-500 uppercase font-bold tracking-wider mb-1 block">Public Key (Simulation)</label>
              <div className="text-teal-400 font-mono text-xs break-all truncate">
                {node.publicKey}
              </div>
               
              <div className="mt-3 flex gap-2 items-center">
                <span className="flex h-2 w-2 relative">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-400"></span>
                </span>
                <span className="text-[10px] text-gray-400">Active on Blockchain</span>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
