// import React, { useEffect, useState } from 'react';
// import NodeAPI from './api';

// export default function RelaySelector() {
//   const [relays, setRelays] = useState([]);
//   const [loading, setLoading] = useState(false);

//   useEffect(() => {
//     loadRelays();
//   }, []);

//   const loadRelays = async () => {
//     setLoading(true);
//     try {
//       const res = await NodeAPI.getRelays();
//       setRelays(res.data || []);
//     } catch (e) {
//       setRelays([]);
//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <section className="card">
//       <div className="card-header">
//         <h2>🧭 Relay Selection</h2>
//       </div>
//       <div className="card-body">
//         {loading && <p>Loading relays...</p>}
//         {!loading && relays.length === 0 && <p>No relay nodes available</p>}
//         {!loading && relays.length > 0 && (
//           <div>
//             <h3>Best Relay: {relays[0].nodeId ? relays[0].nodeId.substring(0, 24) + '...' : 'N/A'}</h3>
//             <ul>
//               {relays.slice(0, 5).map(r => (
//                 <li key={r.nodeId}>{r.nodeId.substring(0, 16)}... — Trust: {r.trustScore}</li>
//               ))}
//             </ul>
//           </div>
//         )}
//       </div>
//     </section>
//   );
// }
import React, { useEffect, useState } from 'react';
import API from './api';

export default function RelaySelector() {

  const [relays, setRelays] = useState([]);

  useEffect(() => {
    loadRelays();
  }, []);

  const loadRelays = async () => {
    try {
      const res = await API.getRelays();
      setRelays(res.data || []);
    } catch (e) {
      setRelays([]);
    }
  };

  const calculateScore = (r) => {
    if (!r.latency || r.latency === 0) return 0;

    return (0.5 * r.packetDeliveryRatio) +
           (0.3 * (1 / r.latency)) +
           (0.2 * (1 - r.failureRate));
  };

  const getBestRelay = () => {

    if (relays.length === 0) return null;

    return relays.reduce((best, r) => {

      const score = calculateScore(r);

      if (!best || score > best.score) {
        return { relay: r, score };
      }

      return best;

    }, null);
  };

  const best = getBestRelay();

  return (
    <section className="card">

      <div className="card-header">
        <h2>🧭 Relay Selection</h2>
      </div>

      <div className="card-body">

        {relays.length === 0 && <p>No relay nodes available</p>}

        {best && (
          <h3>
            Best Relay: {best.relay.nodeId.substring(0, 20)}...
          </h3>
        )}

        <ul>
          {relays.map(r => {

            const score = calculateScore(r);

            return (
              <li key={r.relayId}>
                {r.nodeId.substring(0, 16)}...
                — Trust: {score.toFixed(3)}
              </li>
            );
          })}
        </ul>

      </div>

    </section>
  );
}