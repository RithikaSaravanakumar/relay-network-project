import React, { useState } from 'react';
import NodeAPI from './api';

export default function MessageSender() {
  const [source, setSource] = useState('');
  const [destination, setDestination] = useState('');
  const [content, setContent] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const send = async () => {
    setLoading(true);
    setResult(null);
    try {
      const res = await NodeAPI.sendMessage({ sourceNode: source, destinationNode: destination, content });
      setResult({ success: true, data: res.data.data });
    } catch (e) {
      setResult({ success: false, message: e.response?.data?.message || 'Failed to send' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="card">
      <div className="card-header"><h2>✉️ Send Message</h2></div>
      <div className="card-body">
        <div className="input-group">
          <input placeholder="Source Node ID" value={source} onChange={e=>setSource(e.target.value)} className="input-field" />
          <input placeholder="Destination Node ID" value={destination} onChange={e=>setDestination(e.target.value)} className="input-field" />
        </div>
        <textarea placeholder="Message content" value={content} onChange={e=>setContent(e.target.value)} className="input-field" />
        <button className="btn btn-primary" onClick={send} disabled={loading}>{loading ? 'Sending...' : 'Send Message'}</button>

        {result && result.success && (
          <div className="result-container success">
            <h4>Message sent</h4>
            <p>Message ID: {result.data.messageId}</p>
            <p>Relay Node: {result.data.relayNode || 'Direct'}</p>
            <p>Delivered: {result.data.delivered ? 'Yes' : 'No'}</p>
          </div>
        )}

        {result && !result.success && (
          <div className="result-container error">
            <h4>Error</h4>
            <p>{result.message}</p>
          </div>
        )}
      </div>
    </section>
  );
}
