import React, { useState, useEffect, useCallback } from 'react';
import './index.css';
import API, { calculateReputation } from './api';
import { LineChart, Line, BarChart, Bar, AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

/* ─── STAT CARD ─── */
function StatCard({ icon, label, value, sub, color }) {
  const colors = {
    indigo: 'from-indigo-500 to-purple-600',
    green: 'from-emerald-500 to-teal-600',
    amber: 'from-amber-500 to-orange-600',
    rose: 'from-rose-500 to-pink-600',
  };
  return (
    <div className="glass rounded-2xl p-5 hover:scale-[1.02] transition-transform duration-300">
      <div className="flex items-center gap-3 mb-3">
        <div className={`w-10 h-10 rounded-xl bg-gradient-to-br ${colors[color]} flex items-center justify-center text-lg`}>{icon}</div>
        <span className="text-gray-400 text-sm font-medium">{label}</span>
      </div>
      <p className="text-3xl font-bold text-white">{value}</p>
      {sub && <p className="text-xs text-gray-500 mt-1">{sub}</p>}
    </div>
  );
}

/* ─── MAIN APP ─── */
export default function App() {
  const [darkMode] = useState(true);
  const [activeTab, setActiveTab] = useState('dashboard');

  // Message sending
  const [source, setSource] = useState('');
  const [destination, setDestination] = useState('');
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [trust, setTrust] = useState(0);

  // Data
  const [messages, setMessages] = useState([]);
  const [relays, setRelays] = useState([]);
  const [trustHistory, setTrustHistory] = useState([]);
  const [stats, setStats] = useState({ total: 0, avgLatency: 0, successRate: 0, avgTrust: 0 });

  // Fetch relays
  const fetchRelays = useCallback(async () => {
    try {
      const res = await API.getRelays();
      const data = res.data?.data || res.data || [];
      setRelays(Array.isArray(data) ? data : []);
    } catch (e) { /* ignore */ }
  }, []);

  useEffect(() => { fetchRelays(); }, [fetchRelays]);

  // Auto-refresh every 8 seconds
  useEffect(() => {
    const id = setInterval(fetchRelays, 8000);
    return () => clearInterval(id);
  }, [fetchRelays]);

  // Update stats whenever messages change
  useEffect(() => {
    if (messages.length === 0) return;
    const total = messages.length;
    const delivered = messages.filter(m => m.delivered).length;
    const avgLat = messages.reduce((s, m) => s + (m.latencyMs || 0), 0) / total;
    const avgT = trustHistory.length > 0 ? trustHistory.reduce((s, t) => s + t.trust, 0) / trustHistory.length : 0;
    setStats({ total, avgLatency: avgLat.toFixed(0), successRate: ((delivered / total) * 100).toFixed(0), avgTrust: avgT.toFixed(3) });
  }, [messages, trustHistory]);

  // Send message
  const sendMessage = async () => {
    if (!source || !destination) return;
    setLoading(true);
    setResult(null);
    setTrust(0);
    try {
      const res = await API.sendMessage({ sourceNode: source, destinationNode: destination, content });
      const data = res.data.data;
      setResult({ success: true, data });
      setMessages(prev => [data, ...prev].slice(0, 50));

      try {
        const trustRes = await calculateReputation(data.relayNode);
        const tv = Number(trustRes?.data || 0);
        setTrust(tv);
        setTrustHistory(prev => [...prev, { time: new Date().toLocaleTimeString(), trust: tv, relay: data.relayNode }].slice(-20));
      } catch { setTrust(0); }
    } catch (e) {
      setResult({ success: false, message: e.response?.data?.message || 'Failed to send' });
    } finally { setLoading(false); }
  };

  const tabs = [
    { id: 'dashboard', label: '📊 Dashboard' },
    { id: 'send', label: '✉️ Send' },
    { id: 'relays', label: '🛰️ Relays' },
    { id: 'history', label: '📜 History' },
  ];

  return (
    <div className={`${darkMode ? 'dark' : ''}`}>
      <div className="min-h-screen bg-gradient-to-br from-[#0f0c1a] via-[#13111c] to-[#1a1528] text-white">

        {/* HEADER */}
        <header className="border-b border-white/5 px-6 py-4">
          <div className="max-w-7xl mx-auto flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-500 to-purple-600 flex items-center justify-center text-xl">🔗</div>
              <div>
                <h1 className="text-xl font-bold bg-gradient-to-r from-indigo-400 to-purple-400 bg-clip-text text-transparent">Relay Network</h1>
                <p className="text-xs text-gray-500">Blockchain-Powered Trust System</p>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
              <span className="text-xs text-gray-400">Live</span>
            </div>
          </div>
        </header>

        {/* TAB NAV */}
        <nav className="border-b border-white/5 px-6">
          <div className="max-w-7xl mx-auto flex gap-1">
            {tabs.map(t => (
              <button key={t.id} onClick={() => setActiveTab(t.id)}
                className={`px-4 py-3 text-sm font-medium rounded-t-lg transition-all duration-200 ${activeTab === t.id ? 'bg-white/5 text-indigo-400 border-b-2 border-indigo-500' : 'text-gray-500 hover:text-gray-300'}`}>
                {t.label}
              </button>
            ))}
          </div>
        </nav>

        <main className="max-w-7xl mx-auto px-6 py-6 space-y-6">

          {/* ═══ DASHBOARD TAB ═══ */}
          {activeTab === 'dashboard' && (
            <>
              {/* Stat Cards */}
              <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
                <StatCard icon="📨" label="Total Messages" value={stats.total} sub="Sent in session" color="indigo" />
                <StatCard icon="⚡" label="Avg Latency" value={`${stats.avgLatency}ms`} sub="Lower is better" color="amber" />
                <StatCard icon="✅" label="Success Rate" value={`${stats.successRate}%`} sub="Delivery rate" color="green" />
                <StatCard icon="🛡️" label="Avg Trust" value={stats.avgTrust} sub="Blockchain verified" color="rose" />
              </div>

              {/* Charts Row */}
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
                {/* Trust Over Time */}
                <div className="glass rounded-2xl p-5">
                  <h3 className="text-sm font-semibold text-gray-300 mb-4">📈 Trust Score Over Time</h3>
                  {trustHistory.length > 0 ? (
                    <ResponsiveContainer width="100%" height={220}>
                      <LineChart data={trustHistory}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#2a2640" />
                        <XAxis dataKey="time" tick={{ fill: '#6b7280', fontSize: 10 }} />
                        <YAxis domain={[0, 1]} tick={{ fill: '#6b7280', fontSize: 10 }} />
                        <Tooltip contentStyle={{ background: '#1e1b2e', border: '1px solid #4f46e5', borderRadius: '12px', color: '#fff' }} />
                        <Line type="monotone" dataKey="trust" stroke="#818cf8" strokeWidth={2} dot={{ fill: '#6366f1', r: 4 }} />
                      </LineChart>
                    </ResponsiveContainer>
                  ) : (
                    <div className="h-[220px] flex items-center justify-center text-gray-600 text-sm">Send messages to see trust trends</div>
                  )}
                </div>

                {/* Latency Trends */}
                <div className="glass rounded-2xl p-5">
                  <h3 className="text-sm font-semibold text-gray-300 mb-4">⏱️ Latency Trends</h3>
                  {messages.length > 0 ? (
                    <ResponsiveContainer width="100%" height={220}>
                      <AreaChart data={messages.slice(0, 15).reverse().map((m, i) => ({ msg: `#${i + 1}`, latency: m.latencyMs }))}>
                        <CartesianGrid strokeDasharray="3 3" stroke="#2a2640" />
                        <XAxis dataKey="msg" tick={{ fill: '#6b7280', fontSize: 10 }} />
                        <YAxis tick={{ fill: '#6b7280', fontSize: 10 }} />
                        <Tooltip contentStyle={{ background: '#1e1b2e', border: '1px solid #f59e0b', borderRadius: '12px', color: '#fff' }} />
                        <Area type="monotone" dataKey="latency" stroke="#f59e0b" fill="rgba(245,158,11,0.15)" strokeWidth={2} />
                      </AreaChart>
                    </ResponsiveContainer>
                  ) : (
                    <div className="h-[220px] flex items-center justify-center text-gray-600 text-sm">Send messages to see latency data</div>
                  )}
                </div>
              </div>

              {/* Success vs Failure */}
              <div className="glass rounded-2xl p-5">
                <h3 className="text-sm font-semibold text-gray-300 mb-4">📊 Success vs Failure per Relay</h3>
                {messages.length > 0 ? (
                  <ResponsiveContainer width="100%" height={200}>
                    <BarChart data={Object.values(messages.reduce((acc, m) => {
                      const r = m.relayNode || 'unknown';
                      if (!acc[r]) acc[r] = { relay: r.slice(0, 14), success: 0, failure: 0 };
                      m.delivered ? acc[r].success++ : acc[r].failure++;
                      return acc;
                    }, {}))}>
                      <CartesianGrid strokeDasharray="3 3" stroke="#2a2640" />
                      <XAxis dataKey="relay" tick={{ fill: '#6b7280', fontSize: 10 }} />
                      <YAxis tick={{ fill: '#6b7280', fontSize: 10 }} />
                      <Tooltip contentStyle={{ background: '#1e1b2e', border: '1px solid #6366f1', borderRadius: '12px', color: '#fff' }} />
                      <Bar dataKey="success" fill="#10b981" radius={[4, 4, 0, 0]} />
                      <Bar dataKey="failure" fill="#ef4444" radius={[4, 4, 0, 0]} />
                    </BarChart>
                  </ResponsiveContainer>
                ) : (
                  <div className="h-[200px] flex items-center justify-center text-gray-600 text-sm">Send messages to see relay performance</div>
                )}
              </div>
            </>
          )}

          {/* ═══ SEND TAB ═══ */}
          {activeTab === 'send' && (
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {/* Send Form */}
              <div className="glass rounded-2xl p-6 space-y-4">
                <h2 className="text-lg font-bold flex items-center gap-2">✉️ <span className="bg-gradient-to-r from-indigo-400 to-purple-400 bg-clip-text text-transparent">Send Message</span></h2>

                <div className="space-y-3">
                  <div>
                    <label className="text-xs text-gray-500 mb-1 block">Source Node ID</label>
                    <input value={source} onChange={e => setSource(e.target.value)} placeholder="Enter source node..."
                      className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-sm text-white placeholder-gray-600 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 transition-all" />
                  </div>
                  <div>
                    <label className="text-xs text-gray-500 mb-1 block">Destination Node ID</label>
                    <input value={destination} onChange={e => setDestination(e.target.value)} placeholder="Enter destination node..."
                      className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-sm text-white placeholder-gray-600 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 transition-all" />
                  </div>
                  <div>
                    <label className="text-xs text-gray-500 mb-1 block">Message Content</label>
                    <textarea value={content} onChange={e => setContent(e.target.value)} placeholder="Type your message..." rows={3}
                      className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-sm text-white placeholder-gray-600 focus:outline-none focus:border-indigo-500 focus:ring-1 focus:ring-indigo-500 transition-all resize-none" />
                  </div>
                </div>

                <button onClick={sendMessage} disabled={loading}
                  className="w-full py-3 bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-500 hover:to-purple-500 rounded-xl font-semibold text-sm transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2">
                  {loading ? (
                    <><svg className="animate-spin h-4 w-4" viewBox="0 0 24 24"><circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none"/><path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"/></svg> Sending...</>
                  ) : '🚀 Send Message'}
                </button>
              </div>

              {/* Result Card */}
              <div className="space-y-4">
                {result && result.success && (
                  <div className="glass rounded-2xl p-6 glow-green border border-emerald-500/20">
                    <h3 className="text-emerald-400 font-bold flex items-center gap-2 mb-4">✅ Message Delivered</h3>

                    {/* Visual Flow */}
                    <div className="flex items-center justify-center gap-2 mb-5 py-3 bg-white/5 rounded-xl">
                      <span className="text-xs bg-indigo-500/20 text-indigo-300 px-3 py-1 rounded-full">📤 Source</span>
                      <span className="text-gray-600">→</span>
                      <span className="text-xs bg-purple-500/20 text-purple-300 px-3 py-1 rounded-full">🛰️ {result.data.relayNode}</span>
                      <span className="text-gray-600">→</span>
                      <span className="text-xs bg-emerald-500/20 text-emerald-300 px-3 py-1 rounded-full">📥 Dest</span>
                    </div>

                    <div className="space-y-2 text-sm">
                      <div className="flex justify-between"><span className="text-gray-400">Message ID</span><span className="text-gray-200 font-mono text-xs">{result.data.messageId?.slice(0, 18)}...</span></div>
                      <div className="flex justify-between"><span className="text-gray-400">Relay Used</span><span className="text-purple-300 font-medium">{result.data.relayNode}</span></div>
                      <div className="flex justify-between"><span className="text-gray-400">Latency</span><span className="text-amber-300">{result.data.latencyMs} ms</span></div>
                      <div className="flex justify-between"><span className="text-gray-400">Status</span><span className={result.data.delivered ? 'text-emerald-400' : 'text-red-400'}>{result.data.delivered ? '✅ Delivered' : '❌ Failed'}</span></div>
                      <div className="flex justify-between items-center pt-2 border-t border-white/5">
                        <span className="text-gray-400">Trust Score</span>
                        <div className="flex items-center gap-2">
                          <div className="w-24 h-2 bg-white/10 rounded-full overflow-hidden">
                            <div className="h-full bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full transition-all duration-500" style={{ width: `${Math.min(trust * 100, 100)}%` }}></div>
                          </div>
                          <span className="text-indigo-300 font-bold">{Number(trust).toFixed(3)}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                )}

                {result && !result.success && (
                  <div className="glass rounded-2xl p-6 glow-red border border-red-500/20">
                    <h3 className="text-red-400 font-bold mb-2">❌ Error</h3>
                    <p className="text-gray-400 text-sm">{result.message}</p>
                  </div>
                )}

                {!result && (
                  <div className="glass rounded-2xl p-6 flex flex-col items-center justify-center h-full min-h-[200px]">
                    <span className="text-4xl mb-3">📡</span>
                    <p className="text-gray-500 text-sm">Send a message to see the result</p>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* ═══ RELAYS TAB ═══ */}
          {activeTab === 'relays' && (
            <div className="glass rounded-2xl p-6">
              <h2 className="text-lg font-bold mb-4 flex items-center gap-2">🏆 <span className="bg-gradient-to-r from-amber-400 to-orange-400 bg-clip-text text-transparent">Relay Leaderboard</span></h2>
              {relays.length > 0 ? (
                <div className="space-y-3">
                  {relays
                    .map(r => ({ ...r, score: ((r.packetDeliveryRatio || 0) * 0.5 + (1 / (1 + (r.latency || 1))) * 0.3 + (1 - (r.failureRate || 0)) * 0.2) }))
                    .sort((a, b) => b.score - a.score)
                    .map((relay, i) => (
                      <div key={relay.relayId || i} className="flex items-center gap-4 p-4 bg-white/5 rounded-xl hover:bg-white/8 transition-all">
                        <span className="text-2xl w-8 text-center">{i === 0 ? '🥇' : i === 1 ? '🥈' : i === 2 ? '🥉' : `#${i + 1}`}</span>
                        <div className="flex-1">
                          <div className="flex items-center justify-between mb-1">
                            <span className="text-sm font-semibold text-white">{relay.relayId}</span>
                            <span className={`text-xs px-2 py-0.5 rounded-full ${relay.status === 'ACTIVE' ? 'bg-emerald-500/20 text-emerald-400' : 'bg-red-500/20 text-red-400'}`}>{relay.status}</span>
                          </div>
                          <div className="w-full h-2 bg-white/10 rounded-full overflow-hidden">
                            <div className="h-full bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full transition-all duration-700" style={{ width: `${(relay.score * 100).toFixed(0)}%` }}></div>
                          </div>
                          <div className="flex justify-between mt-1 text-xs text-gray-500">
                            <span>PDR: {((relay.packetDeliveryRatio || 0) * 100).toFixed(0)}%</span>
                            <span>Latency: {(relay.latency || 0).toFixed(0)}ms</span>
                            <span>Fail: {((relay.failureRate || 0) * 100).toFixed(0)}%</span>
                            <span className="text-indigo-400 font-medium">Score: {relay.score.toFixed(3)}</span>
                          </div>
                        </div>
                      </div>
                    ))}
                </div>
              ) : (
                <div className="text-center py-12 text-gray-600">
                  <span className="text-4xl block mb-3">🛰️</span>
                  <p className="text-sm">No relay nodes found. Send messages to create relays.</p>
                </div>
              )}
            </div>
          )}

          {/* ═══ HISTORY TAB ═══ */}
          {activeTab === 'history' && (
            <div className="glass rounded-2xl p-6">
              <h2 className="text-lg font-bold mb-4 flex items-center gap-2">📜 <span className="bg-gradient-to-r from-indigo-400 to-cyan-400 bg-clip-text text-transparent">Message History</span></h2>
              {messages.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b border-white/10">
                        <th className="text-left py-3 px-3 text-gray-500 font-medium text-xs">#</th>
                        <th className="text-left py-3 px-3 text-gray-500 font-medium text-xs">Message ID</th>
                        <th className="text-left py-3 px-3 text-gray-500 font-medium text-xs">Relay</th>
                        <th className="text-left py-3 px-3 text-gray-500 font-medium text-xs">Latency</th>
                        <th className="text-left py-3 px-3 text-gray-500 font-medium text-xs">Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {messages.map((m, i) => (
                        <tr key={m.messageId || i} className="border-b border-white/5 hover:bg-white/5 transition-colors">
                          <td className="py-3 px-3 text-gray-600">{i + 1}</td>
                          <td className="py-3 px-3 font-mono text-xs text-gray-300">{m.messageId?.slice(0, 12)}...</td>
                          <td className="py-3 px-3 text-purple-300">{m.relayNode}</td>
                          <td className="py-3 px-3 text-amber-300">{m.latencyMs}ms</td>
                          <td className="py-3 px-3">
                            <span className={`px-2 py-0.5 rounded-full text-xs ${m.delivered ? 'bg-emerald-500/20 text-emerald-400' : 'bg-red-500/20 text-red-400'}`}>
                              {m.delivered ? 'Delivered' : 'Failed'}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="text-center py-12 text-gray-600">
                  <span className="text-4xl block mb-3">📭</span>
                  <p className="text-sm">No messages sent yet. Go to Send tab to start.</p>
                </div>
              )}
            </div>
          )}

        </main>

        {/* FOOTER */}
        <footer className="border-t border-white/5 px-6 py-4 mt-8">
          <div className="max-w-7xl mx-auto flex items-center justify-between text-xs text-gray-600">
            <span>Blockchain Relay Network © 2026</span>
            <span>Backend: <code className="text-indigo-400">localhost:8080</code> | Ganache: <code className="text-indigo-400">localhost:7545</code></span>
          </div>
        </footer>
      </div>
    </div>
  );
}