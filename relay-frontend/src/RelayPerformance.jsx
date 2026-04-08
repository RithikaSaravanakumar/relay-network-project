// // import React, { useState } from 'react';
// // import API from './api';

// // export default function RelayPerformance() {
// //   const [relayId, setRelayId] = useState('');
// //   const [latency, setLatency] = useState('');
// //   const [pdr, setPdr] = useState('');
// //   const [failure, setFailure] = useState('');
// //   const [result, setResult] = useState(null);

// //   const submit = async () => {
// //     setResult(null);
// //     try {
// //       const payload = {
// //         relayId,
// //         latency: latency ? parseFloat(latency) : null,
// //         packetDeliveryRatio: pdr ? parseFloat(pdr) : null,
// //         failureRate: failure ? parseFloat(failure) : null,
// //       };
// //       const res = await API.updateRelayPerformance(payload);
// //       setResult({ success: true, data: res.data.data });
// //     } catch (e) {
// //       setResult({ success: false, message: e.response?.data?.message || 'Failed' });
// //     }
// //   };

// //   return (
// //     <section className="card">
// //       <div className="card-header"><h2>⚙️ Relay Performance</h2></div>
// //       <div className="card-body">
// //         <div className="input-group">
// //           <input placeholder="Relay ID" value={relayId} onChange={e=>setRelayId(e.target.value)} className="input-field" />
// //           <input placeholder="Latency (ms)" value={latency} onChange={e=>setLatency(e.target.value)} className="input-field" />
// //           <input placeholder="Packet Delivery Ratio (0-1)" value={pdr} onChange={e=>setPdr(e.target.value)} className="input-field" />
// //           <input placeholder="Failure Rate (0-1)" value={failure} onChange={e=>setFailure(e.target.value)} className="input-field" />
// //         </div>
// //         <button className="btn btn-primary" onClick={submit}>Update Performance</button>

// //         {result && result.success && (
// //           <div className="result-container success">
// //             <pre>{JSON.stringify(result.data, null, 2)}</pre>
// //           </div>
// //         )}
// //         {result && !result.success && (
// //           <div className="result-container error">
// //             <p>{result.message}</p>
// //           </div>
// //         )}
// //       </div>
// //     </section>
// //   );
// // }
// import React, { useEffect, useState } from "react";
// import API from "./api";

// export default function RelayPerformance() {

//   const [relays, setRelays] = useState([]);
//   const [relayId, setRelayId] = useState("");
//   const [latency, setLatency] = useState("");
//   const [pdr, setPdr] = useState("");
//   const [failure, setFailure] = useState("");
//   const [result, setResult] = useState(null);

//   useEffect(() => {
//     loadRelays();
//   }, []);

//   const loadRelays = async () => {
//     try {
//       const res = await API.getRelays();
//       setRelays(res.data.data || []);
//     } catch (e) {
//       setRelays([]);
//     }
//   };

//   const submit = async () => {

//     const payload = {
//       relayId,
//       latency: latency ? parseFloat(latency) : null,
//       packetDeliveryRatio: pdr ? parseFloat(pdr) : null,
//       failureRate: failure ? parseFloat(failure) : null
//     };

//     try {

//       const res = await API.updateRelayPerformance(payload);

//       setResult({
//         success:true,
//         data:res.data.data
//       });

//     } catch(e){

//       setResult({
//         success:false,
//         message:"Update failed"
//       });

//     }
//   };

//   return (
//     <section className="card">

//       <div className="card-header">
//         ⚙️ Relay Performance
//       </div>

//       <div className="card-body">

//         {/* Relay selector */}
//         <select
//           value={relayId}
//           onChange={(e)=>setRelayId(e.target.value)}
//           className="input-field"
//         >

//           <option value="">Select Relay</option>

//           {relays.map(r => (

//             <option key={r.relayId} value={r.relayId}>

//               {r.relayId} (Node {r.nodeId?.substring(0,10)}...)

//             </option>

//           ))}

//         </select>

//         <input
//           placeholder="Latency (ms)"
//           value={latency}
//           onChange={e=>setLatency(e.target.value)}
//           className="input-field"
//         />

//         <input
//           placeholder="Packet Delivery Ratio (0-1)"
//           value={pdr}
//           onChange={e=>setPdr(e.target.value)}
//           className="input-field"
//         />

//         <input
//           placeholder="Failure Rate (0-1)"
//           value={failure}
//           onChange={e=>setFailure(e.target.value)}
//           className="input-field"
//         />

//         <button className="btn btn-primary" onClick={submit}>
//           Update Performance
//         </button>

//         {result && result.success && (
//           <div style={{marginTop:10,color:"green"}}>
//             Updated successfully
//           </div>
//         )}

//         {result && !result.success && (
//           <div style={{marginTop:10,color:"red"}}>
//             {result.message}
//           </div>
//         )}

//       </div>

//     </section>
//   );
// }

import React, { useEffect, useState } from "react";
import API from "./api";

export default function RelayPerformance(){

const [relays,setRelays]=useState([]);
const [relayId,setRelayId]=useState("");
const [latency,setLatency]=useState("");
const [pdr,setPdr]=useState("");
const [failure,setFailure]=useState("");

useEffect(()=>{
loadRelays();
},[]);

const loadRelays = async () => {

try{

const res = await API.getRelays();

console.log(res.data);

setRelays(res.data.data || res.data);

}catch(e){

console.log(e);

}

};

const update = async () => {

const payload = {

relayId,
latency:parseFloat(latency),
packetDeliveryRatio:parseFloat(pdr),
failureRate:parseFloat(failure)

};

await API.updateRelayPerformance(payload);

alert("Performance Updated");

};

return(

<section className="card">

<div className="card-header">
⚙ Relay Performance
</div>

<div className="card-body">

<select
className="input-field"
value={relayId}
onChange={(e)=>setRelayId(e.target.value)}
>

<option value="">Select Relay</option>

{relays.map((r)=>(

<option key={r.relayId} value={r.relayId}>

{r.relayId}

</option>

))}

</select>

<input
className="input-field"
placeholder="Latency"
value={latency}
onChange={(e)=>setLatency(e.target.value)}
/>

<input
className="input-field"
placeholder="Packet Delivery Ratio"
value={pdr}
onChange={(e)=>setPdr(e.target.value)}
/>

<input
className="input-field"
placeholder="Failure Rate"
value={failure}
onChange={(e)=>setFailure(e.target.value)}
/>

<button className="btn btn-primary" onClick={update}>
Update Performance
</button>

</div>

</section>

);

}