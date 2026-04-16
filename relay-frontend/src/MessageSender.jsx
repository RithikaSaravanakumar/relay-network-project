// // // // import React, { useState } from 'react';
// // // // import NodeAPI from './api';

// // // // export default function MessageSender() {
// // // //   const [source, setSource] = useState('');
// // // //   const [destination, setDestination] = useState('');
// // // //   const [content, setContent] = useState('');
// // // //   const [result, setResult] = useState(null);
// // // //   const [loading, setLoading] = useState(false);

// // // //   const send = async () => {
// // // //     setLoading(true);
// // // //     setResult(null);
// // // //     try {
// // // //       const res = await NodeAPI.sendMessage({ sourceNode: source, destinationNode: destination, content });
// // // //       setResult({ success: true, data: res.data.data });
// // // //     } catch (e) {
// // // //       setResult({ success: false, message: e.response?.data?.message || 'Failed to send' });
// // // //     } finally {
// // // //       setLoading(false);
// // // //     }
// // // //   };

// // // //   return (
// // // //     <section className="card">
// // // //       <div className="card-header"><h2>✉️ Send Message</h2></div>
// // // //       <div className="card-body">
// // // //         <div className="input-group">
// // // //           <input placeholder="Source Node ID" value={source} onChange={e=>setSource(e.target.value)} className="input-field" />
// // // //           <input placeholder="Destination Node ID" value={destination} onChange={e=>setDestination(e.target.value)} className="input-field" />
// // // //         </div>
// // // //         <textarea placeholder="Message content" value={content} onChange={e=>setContent(e.target.value)} className="input-field" />
// // // //         <button className="btn btn-primary" onClick={send} disabled={loading}>{loading ? 'Sending...' : 'Send Message'}</button>

// // // //         {result && result.success && (
// // // //           <div className="result-container success">
// // // //             <h4>Message sent</h4>
// // // //             <p>Message ID: {result.data.messageId}</p>
// // // //             <p>Relay Node: {result.data.relayNode || 'Direct'}</p>
// // // //             <p>Delivered: {result.data.delivered ? 'Yes' : 'No'}</p>
// // // //           </div>
// // // //         )}

// // // //         {result && !result.success && (
// // // //           <div className="result-container error">
// // // //             <h4>Error</h4>
// // // //             <p>{result.message}</p>
// // // //           </div>
// // // //         )}
// // // //       </div>
// // // //     </section>
// // // //   );
// // // // }
// // // import React, { useState } from 'react';
// // // import API, { calculateReputation } from './api';

// // // export default function MessageSender() {

// // //   const [source, setSource] = useState('');
// // //   const [destination, setDestination] = useState('');
// // //   const [content, setContent] = useState('');

// // //   const [result, setResult] = useState(null);
// // //   const [trust, setTrust] = useState(null);
// // //   const [loading, setLoading] = useState(false);

// // //   const send = async () => {

// // //     setLoading(true);
// // //     setResult(null);
// // //     setTrust(null);

// // //     try {

// // //       const res = await API.sendMessage({
// // //         sourceNode: source,
// // //         destinationNode: destination,
// // //         content
// // //       });

// // //       const data = res.data.data;

// // //       setResult({ success: true, data });

// // //       // 🔥 Fetch trust score
// // //       const trustRes = await calculateReputation(data.relayNode);

// // //       setTrust(trustRes.data.reputationScore);

// // //     } catch (e) {

// // //       setResult({
// // //         success: false,
// // //         message: e.response?.data?.message || 'Failed to send'
// // //       });

// // //     } finally {
// // //       setLoading(false);
// // //     }
// // //   };

// // //   return (
// // //     <section className="card">

// // //       <div className="card-header">
// // //         <h2>✉️ Send Message</h2>
// // //       </div>

// // //       <div className="card-body">

// // //         <input
// // //           placeholder="Source Node ID"
// // //           value={source}
// // //           onChange={e => setSource(e.target.value)}
// // //           className="input-field"
// // //         />

// // //         <input
// // //           placeholder="Destination Node ID"
// // //           value={destination}
// // //           onChange={e => setDestination(e.target.value)}
// // //           className="input-field"
// // //         />

// // //         <textarea
// // //           placeholder="Message content"
// // //           value={content}
// // //           onChange={e => setContent(e.target.value)}
// // //           className="input-field"
// // //         />

// // //         <button
// // //           className="btn btn-primary"
// // //           onClick={send}
// // //           disabled={loading}
// // //         >
// // //           {loading ? 'Sending...' : 'Send Message'}
// // //         </button>

// // //         {/* ✅ RESULT DISPLAY */}

// // //         {result && result.success && (

// // //           <div className="result-container success">

// // //             <h4>✅ Message Sent Successfully</h4>

// // //             <p><strong>Message ID:</strong> {result.data.messageId}</p>

// // //             <p><strong>Relay Used:</strong> {result.data.relayNode}</p>

// // //             <p><strong>Latency:</strong> {result.data.latencyMs} ms</p>

// // //             <p>
// // //               <strong>Status:</strong>
// // //               {result.data.delivered ? " ✅ Delivered" : " ❌ Failed"}
// // //             </p>

// // //             {trust !== null && (
// // //               <p><strong>Trust Score:</strong> {trust.toFixed(3)}</p>
// // //             )}

// // //           </div>
// // //         )}

// // //         {result && !result.success && (

// // //           <div className="result-container error">

// // //             <h4>❌ Error</h4>
// // //             <p>{result.message}</p>

// // //           </div>
// // //         )}

// // //       </div>

// // //     </section>
// // //   );
// // // }
// // import React, { useState } from 'react';
// // import API, { calculateReputation } from './api';

// // export default function MessageSender() {

// //   const [source, setSource] = useState('');
// //   const [destination, setDestination] = useState('');
// //   const [content, setContent] = useState('');

// //   const [result, setResult] = useState(null);
// //   const [trust, setTrust] = useState(null);
// //   const [loading, setLoading] = useState(false);

// //   const send = async () => {

// //     setLoading(true);
// //     setResult(null);
// //     setTrust(null);

// //     try {

// //       const res = await API.sendMessage({
// //         sourceNode: source,
// //         destinationNode: destination,
// //         content
// //       });

// //       const data = res.data.data;

// //       setResult({ success: true, data });

// //       // 🔥 Fetch trust score
// //       const trustRes = await calculateReputation(data.relayNode);

// //       setTrust(trustRes.data.reputationScore);

// //     } catch (e) {

// //       setResult({
// //         success: false,
// //         message: e.response?.data?.message || 'Failed to send'
// //       });

// //     } finally {
// //       setLoading(false);
// //     }
// //   };

// //   return (
// //     <section className="card">

// //       <div className="card-header">
// //         <h2>✉️ Send Message</h2>
// //       </div>

// //       <div className="card-body">

// //         <input
// //           placeholder="Source Node ID"
// //           value={source}
// //           onChange={e => setSource(e.target.value)}
// //           className="input-field"
// //         />

// //         <input
// //           placeholder="Destination Node ID"
// //           value={destination}
// //           onChange={e => setDestination(e.target.value)}
// //           className="input-field"
// //         />

// //         <textarea
// //           placeholder="Message content"
// //           value={content}
// //           onChange={e => setContent(e.target.value)}
// //           className="input-field"
// //         />

// //         <button
// //           className="btn btn-primary"
// //           onClick={send}
// //           disabled={loading}
// //         >
// //           {loading ? 'Sending...' : 'Send Message'}
// //         </button>

// //         {/* ✅ RESULT DISPLAY */}

// //         {result && result.success && (

// //           <div className="result-container success">

// //             <h4>✅ Message Sent Successfully</h4>

// //             <p><strong>Message ID:</strong> {result.data.messageId}</p>

// //             <p><strong>Relay Used:</strong> {result.data.relayNode}</p>

// //             <p><strong>Latency:</strong> {result.data.latencyMs} ms</p>

// //             <p>
// //               <strong>Status:</strong>
// //               {result.data.delivered ? " ✅ Delivered" : " ❌ Failed"}
// //             </p>

// //             {trust !== null && (
               
// //               <p><strong>Trust Score:</strong> {Number(trust).toFixed(3)}</p>
// //             )}

// //           </div>
// //         )}

// //         {result && !result.success && (

// //           <div className="result-container error">

// //             <h4>❌ Error</h4>
// //             <p>{result.message}</p>

// //           </div>
// //         )}

// //       </div>

// //     </section>
// //   );
// // }
// import React, { useState } from 'react';
// import API, { calculateReputation } from './api';

// export default function MessageSender() {

//   const [source, setSource] = useState('');
//   const [destination, setDestination] = useState('');
//   const [content, setContent] = useState('');

//   const [result, setResult] = useState(null);
//   const [trust, setTrust] = useState(0); // ✅ FIXED
//   const [loading, setLoading] = useState(false);

//   const send = async () => {

//     setLoading(true);
//     setResult(null);
//     setTrust(0); // ✅ reset safely

//     try {

//       const res = await API.sendMessage({
//         sourceNode: source,
//         destinationNode: destination,
//         content
//       });

//       const data = res.data.data;

//       setResult({ success: true, data });

//       // 🔥 Fetch trust score (FIXED)
//       try {
//         const trustRes = await calculateReputation(data.relayNode);

//         const trustValue = trustRes?.data;

//         setTrust(Number(trustValue || 0));

//       } catch (err) {
//         console.log("Trust fetch failed:", err);
//         setTrust(0);
//       }

//     } catch (e) {

//       setResult({
//         success: false,
//         message: e.response?.data?.message || 'Failed to send'
//       });

//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <section className="card">

//       <div className="card-header">
//         <h2>✉️ Send Message</h2>
//       </div>

//       <div className="card-body">

//         <input
//           placeholder="Source Node ID"
//           value={source}
//           onChange={e => setSource(e.target.value)}
//           className="input-field"
//         />

//         <input
//           placeholder="Destination Node ID"
//           value={destination}
//           onChange={e => setDestination(e.target.value)}
//           className="input-field"
//         />

//         <textarea
//           placeholder="Message content"
//           value={content}
//           onChange={e => setContent(e.target.value)}
//           className="input-field"
//         />

//         <button
//           className="btn btn-primary"
//           onClick={send}
//           disabled={loading}
//         >
//           {loading ? 'Sending...' : 'Send Message'}
//         </button>

//         {/* ✅ RESULT DISPLAY */}

//         {result && result.success && (

//           <div className="result-container success">

//             <h4>✅ Message Sent Successfully</h4>

//             <p><strong>Message ID:</strong> {result?.data?.messageId}</p>

//             <p><strong>Relay Used:</strong> {result?.data?.relayNode}</p>

//             <p>
//               <strong>Latency:</strong>{" "}
//               {Number(result?.data?.latencyMs ?? 0)} ms
//             </p>

//             <p>
//               <strong>Status:</strong>
//               {result?.data?.delivered ? " ✅ Delivered" : " ❌ Failed"}
//             </p>

//             {/* 🔥 SAFE TRUST DISPLAY */}
//             <p>
//               <strong>Trust Score:</strong>{" "}
//               {Number(trust ?? 0).toFixed(3)}
//             </p>

//           </div>
//         )}

//         {result && !result.success && (

//           <div className="result-container error">

//             <h4>❌ Error</h4>
//             <p>{result.message}</p>

//           </div>
//         )}

//       </div>

//     </section>
//   );
// }
import React, { useState } from 'react';
import API, { calculateReputation } from './api';

export default function MessageSender() {

  const [source, setSource] = useState('');
  const [destination, setDestination] = useState('');
  const [content, setContent] = useState('');

  const [result, setResult] = useState(null);
  const [trust, setTrust] = useState(0);
  const [loading, setLoading] = useState(false);

  const send = async () => {

    setLoading(true);
    setResult(null);
    setTrust(0);

    try {

      const res = await API.sendMessage({
        sourceNode: source,
        destinationNode: destination,
        content
      });

      const data = res.data.data;

      setResult({ success: true, data });

      // 🔥 FIXED TRUST FETCH
      try {
        const trustRes = await calculateReputation(data.relayNode);
        const trustValue = trustRes?.data;

        setTrust(Number(trustValue || 0));

      } catch (err) {
        console.log("Trust fetch failed:", err);
        setTrust(0);
      }

    } catch (e) {

      setResult({
        success: false,
        message: e.response?.data?.message || 'Failed to send'
      });

    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="card">

      <div className="card-header">
        <h2>✉️ Send Message</h2>
      </div>

      <div className="card-body">

        <input
          placeholder="Source Node ID"
          value={source}
          onChange={e => setSource(e.target.value)}
          className="input-field"
        />

        <input
          placeholder="Destination Node ID"
          value={destination}
          onChange={e => setDestination(e.target.value)}
          className="input-field"
        />

        <textarea
          placeholder="Message content"
          value={content}
          onChange={e => setContent(e.target.value)}
          className="input-field"
        />

        <button onClick={send} disabled={loading}>
          {loading ? 'Sending...' : 'Send Message'}
        </button>

        {result && result.success && (
          <div>
            <h4>✅ Message Sent Successfully</h4>

            <p><strong>Message ID:</strong> {result.data.messageId}</p>
            <p><strong>Relay Used:</strong> {result.data.relayNode}</p>
            <p><strong>Latency:</strong> {result.data.latencyMs} ms</p>

            <p>
              <strong>Status:</strong>
              {result.data.delivered ? " ✅ Delivered" : " ❌ Failed"}
            </p>

            <p>
              <strong>Trust Score:</strong>{" "}
              {Number(trust ?? 0).toFixed(3)}
            </p>
          </div>
        )}

        {result && !result.success && (
          <div>
            <h4>❌ Error</h4>
            <p>{result.message}</p>
          </div>
        )}

      </div>
    </section>
  );
}