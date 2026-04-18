import React from 'react';

export default function IdentityPanel({ relays }) {
  return (
    <div className="bg-surface-800 p-6 rounded-2xl border border-white/5 shadow-xl transition-all h-full">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-xl font-bold bg-gradient-to-r from-primary-400 to-accent-400 bg-clip-text text-transparent">
          Node Identity Registry
        </h2>
        <span className="px-3 py-1 bg-primary-500/10 text-primary-400 text-xs rounded-full border border-primary-500/20">
          Blockchain verification
        </span>
      </div>
      
      <p className="text-sm text-gray-400 mb-6">
        Relay identities are generated using SHA-256 and securely committed to the <b>NodeRegistry.sol</b> smart contract along with their Public Keys.
      </p>

      <div className="space-y-3 overflow-y-auto max-h-64 pr-2 custom-scrollbar">
        {relays.map((r, i) => (
          <div key={i} className="p-3 bg-surface-900 rounded-lg border border-white/5 hover:border-primary-500/30 transition-colors">
            <div className="flex justify-between items-start mb-2">
              <span className="text-xs font-mono text-primary-300 font-semibold">{r.relayId}</span>
              {r.status === 'BLOCKED' ? (
                <span className="text-[10px] uppercase font-bold text-red-400 bg-red-400/10 px-2 py-0.5 rounded">Blocked</span>
              ) : (
                <span className="text-[10px] uppercase font-bold text-green-400 bg-green-400/10 px-2 py-0.5 rounded">Active</span>
              )}
            </div>
            <div className="text-[11px] text-gray-500 font-mono break-all">
              Key: {r.publicKey || "Pending..."}
            </div>
          </div>
        ))}
        {relays.length === 0 && (
          <div className="text-center text-gray-500 text-sm py-4">
            No identities registered yet.
          </div>
        )}
      </div>
    </div>
  );
}
