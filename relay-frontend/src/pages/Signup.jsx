import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

export default function Signup() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await axios.post('http://localhost:8080/api/auth/signup', { username, password });
      localStorage.setItem('relay_token', res.data.token);
      localStorage.setItem('relay_user', JSON.stringify(res.data));
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to register');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#0f0c1a] via-[#13111c] to-[#1a1528] text-white">
      <div className="bg-surface-800 p-8 rounded-2xl border border-white/5 shadow-2xl w-96">
        <h2 className="text-2xl font-bold bg-gradient-to-r from-emerald-400 to-teal-400 bg-clip-text text-transparent mb-6 text-center">Create Identity</h2>
        {error && <div className="bg-red-500/20 text-red-400 p-3 rounded mb-4 text-sm">{error}</div>}
        <form onSubmit={handleSignup} className="space-y-4">
          <div>
            <label className="text-xs text-gray-500">Username</label>
            <input type="text" value={username} onChange={e => setUsername(e.target.value)} required
              className="w-full px-4 py-2 bg-white/5 border border-white/10 rounded-lg text-sm focus:border-emerald-500 outline-none" />
          </div>
          <div>
            <label className="text-xs text-gray-500">Password</label>
             <input type="password" value={password} onChange={e => setPassword(e.target.value)} required
              className="w-full px-4 py-2 bg-white/5 border border-white/10 rounded-lg text-sm focus:border-emerald-500 outline-none" />
          </div>
          <button disabled={loading} className="w-full py-2 bg-emerald-600 hover:bg-emerald-500 rounded-lg font-bold transition-colors disabled:opacity-50">
            {loading ? 'Registering Node...' : 'Signup & Generate Node Keys'}
          </button>
        </form>
        <div className="mt-4 text-center text-xs text-gray-500">
          Already registered? <Link to="/login" className="text-emerald-400 hover:underline">Login here</Link>
        </div>
      </div>
    </div>
  );
}
