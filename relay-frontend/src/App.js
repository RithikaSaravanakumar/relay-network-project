// import React, { useState } from 'react';
// import NodeAPI from './api';
// import './App.css';
// import NetworkNodes from './NetworkNodes';
// import RelaySelector from './RelaySelector';
// import MessageSender from './MessageSender';
// import TrustScoreMonitor from './TrustScoreMonitor';
// import RelayNodes from './RelayNodes';
// import RelayPerformance from './RelayPerformance';
// import ReputationMonitor from './ReputationMonitor';

// function App() {
//   // State for node registration
//   const [registeredNode, setRegisteredNode] = useState(null);
//   const [registrationLoading, setRegistrationLoading] = useState(false);
//   const [registrationError, setRegistrationError] = useState(null);

//   // State for health check
//   const [healthStatus, setHealthStatus] = useState(null);
//   const [healthLoading, setHealthLoading] = useState(false);
//   const [healthError, setHealthError] = useState(null);

//   // State for node lookup
//   const [lookupNodeId, setLookupNodeId] = useState('');
//   const [lookedUpNode, setLookedUpNode] = useState(null);
//   const [lookupLoading, setLookupLoading] = useState(false);
//   const [lookupError, setLookupError] = useState(null);

//   /**
//    * Handle node registration
//    */
//   const handleRegisterNode = async () => {
//     setRegistrationLoading(true);
//     setRegistrationError(null);
//     setRegisteredNode(null);

//     try {
//       const response = await NodeAPI.registerNode();
//       setRegisteredNode(response.data.data);
//       console.log('Node registered:', response.data);
//     } catch (error) {
//       setRegistrationError(
//         error.response?.data?.message || 'Failed to register node. Check if backend is running.'
//       );
//       console.error('Registration error:', error);
//     } finally {
//       setRegistrationLoading(false);
//     }
//   };

//   /**
//    * Handle health check
//    */
//   const handleCheckHealth = async () => {
//     setHealthLoading(true);
//     setHealthError(null);
//     setHealthStatus(null);

//     try {
//       const response = await NodeAPI.checkHealth();
//       setHealthStatus(response.data);
//       console.log('Health status:', response.data);
//     } catch (error) {
//       setHealthError(
//         error.response?.data?.message || 'Failed to check health. Server may be offline.'
//       );
//       console.error('Health check error:', error);
//     } finally {
//       setHealthLoading(false);
//     }
//   };

//   /**
//    * Handle node lookup
//    */
//   const handleLookupNode = async () => {
//     if (!lookupNodeId.trim()) {
//       setLookupError('Please enter a node ID');
//       return;
//     }

//     setLookupLoading(true);
//     setLookupError(null);
//     setLookedUpNode(null);

//     try {
//       const response = await NodeAPI.getNode(lookupNodeId);
//       setLookedUpNode(response.data.data);
//       console.log('Node lookup result:', response.data);
//     } catch (error) {
//       setLookupError(
//         error.response?.data?.message || `Node not found with ID: ${lookupNodeId}`
//       );
//       console.error('Lookup error:', error);
//     } finally {
//       setLookupLoading(false);
//     }
//   };

//   /**
//    * Format nodeId for display (show first 20 and last 20 characters)
//    */
//   const formatNodeId = (nodeId) => {
//     if (!nodeId || nodeId.length <= 40) return nodeId;
//     return `${nodeId.substring(0, 20)}...${nodeId.substring(nodeId.length - 20)}`;
//   };

//   return (
//     <div className="app-container">
//       <header className="app-header">
//         <h1>🔗 Relay Node Dashboard</h1>
//         <p>Blockchain-Based Reputation-Aware Relay Selection for Web3 Networks</p>
//       </header>

//       <main className="app-main">
//         {/* Health Check Section */}
//         <section className="card">
//           <div className="card-header">
//             <h2>🏥 Server Health</h2>
//           </div>
//           <div className="card-body">
//             <button
//               onClick={handleCheckHealth}
//               disabled={healthLoading}
//               className="btn btn-primary"
//             >
//               {healthLoading ? 'Checking...' : 'Check Server Health'}
//             </button>

//             {healthStatus && (
//               <div className="result-container success">
//                 <h3>✅ Server Status</h3>
//                 <div className="result-details">
//                   <p>
//                     <strong>Service:</strong> {healthStatus.service}
//                   </p>
//                   <p>
//                     <strong>Status:</strong>
//                     <span className="badge badge-success">{healthStatus.status}</span>
//                   </p>
//                 </div>
//               </div>
//             )}

//             {healthError && (
//               <div className="result-container error">
//                 <h3>❌ Error</h3>
//                 <p>{healthError}</p>
//               </div>
//             )}
//           </div>
//         </section>

//         {/* Node Registration Section */}
//         <section className="card">
//           <div className="card-header">
//             <h2>➕ Register Node</h2>
//           </div>
//           <div className="card-body">
//             <p className="info-text">
//               Register a new node in the P2P relay network. The system will automatically
//               generate RSA keys and node identity.
//             </p>
//             <button
//               onClick={handleRegisterNode}
//               disabled={registrationLoading}
//               className="btn btn-success"
//             >
//               {registrationLoading ? 'Registering...' : 'Register Node'}
//             </button>

//             {registeredNode && (
//               <div className="result-container success">
//                 <h3>✅ Node Registered Successfully</h3>
//                 <div className="result-details">
//                   <div className="detail-row">
//                     <strong>Node ID:</strong>
//                     <code className="code-block">{formatNodeId(registeredNode.nodeId)}</code>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Public Key:</strong>
//                     <code className="code-block">{formatNodeId(registeredNode.publicKey)}</code>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Trust Score:</strong>
//                     <span className="badge badge-info">{registeredNode.trustScore}</span>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Malicious:</strong>
//                     <span
//                       className={`badge ${registeredNode.malicious ? 'badge-danger' : 'badge-success'}`}
//                     >
//                       {registeredNode.malicious ? 'Yes' : 'No'}
//                     </span>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Database ID:</strong>
//                     <code className="code-block">{registeredNode.databaseId}</code>
//                   </div>
//                 </div>
//               </div>
//             )}

//             {registrationError && (
//               <div className="result-container error">
//                 <h3>❌ Error</h3>
//                 <p>{registrationError}</p>
//               </div>
//             )}
//           </div>
//         </section>

//           {/* Node Lookup Section */}
//         <section className="card">
//           <div className="card-header">
//             <h2>🔍 Node Lookup</h2>
//           </div>
//           <div className="card-body">
//             <p className="info-text">
//               Enter a node ID to retrieve detailed information about a registered node.
//             </p>
//             <div className="input-group">
//               <input
//                 type="text"
//                 placeholder="Enter Node ID (SHA-256 hash)"
//                 value={lookupNodeId}
//                 onChange={(e) => setLookupNodeId(e.target.value)}
//                 onKeyPress={(e) => e.key === 'Enter' && handleLookupNode()}
//                 className="input-field"
//               />
//               <button
//                 onClick={handleLookupNode}
//                 disabled={lookupLoading}
//                 className="btn btn-info"
//               >
//                 {lookupLoading ? 'Looking up...' : 'Search'}
//               </button>
//             </div>

//             {lookedUpNode && (
//               <div className="result-container success">
//                 <h3>✅ Node Found</h3>
//                 <div className="result-details">
//                   <div className="detail-row">
//                     <strong>Node ID:</strong>
//                     <code className="code-block">{formatNodeId(lookedUpNode.nodeId)}</code>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Public Key:</strong>
//                     <code className="code-block">{formatNodeId(lookedUpNode.publicKey)}</code>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Trust Score:</strong>
//                     <span className="badge badge-info">{lookedUpNode.trustScore}</span>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Malicious:</strong>
//                     <span
//                       className={`badge ${lookedUpNode.malicious ? 'badge-danger' : 'badge-success'}`}
//                     >
//                       {lookedUpNode.malicious ? 'Yes' : 'No'}
//                     </span>
//                   </div>
//                   <div className="detail-row">
//                     <strong>Database ID:</strong>
//                     <code className="code-block">{lookedUpNode.databaseId}</code>
//                   </div>
//                 </div>
//               </div>
//             )}

//             {lookupError && (
//               <div className="result-container error">
//                 <h3>❌ Error</h3>
//                 <p>{lookupError}</p>
//               </div>
//             )}
//           </div>
//         </section>

//         {/* New network components */}
//         <NetworkNodes />
//         <RelayNodes />
//         <RelaySelector />
//         <RelayPerformance />
//         <ReputationMonitor />
//         <TrustScoreMonitor />
//         <MessageSender />
//       </main>

//       <footer className="app-footer">
//         <p>
//           Backend API: <code>http://localhost:8080</code>
//         </p>
//         <p>© 2026 Relay Node Network | Web3 P2P Communication</p>
//       </footer>
//     </div>
//   );
// }

// export default App;

import React, { useState } from "react";
import NodeAPI from "./api";
import "./App.css";

import NetworkNodes from "./NetworkNodes";
import RelayNodes from "./RelayNodes";
import RelaySelector from "./RelaySelector";
import MessageSender from "./MessageSender";
import RelayPerformance from "./RelayPerformance";
import ReputationMonitor from "./ReputationMonitor";
import TrustScoreMonitor from "./TrustScoreMonitor";

function App() {

  const [activeTab, setActiveTab] = useState("dashboard");

  const [registeredNode, setRegisteredNode] = useState(null);
  const [registrationLoading, setRegistrationLoading] = useState(false);

  const [healthStatus, setHealthStatus] = useState(null);
  const [healthLoading, setHealthLoading] = useState(false);

  const [lookupNodeId, setLookupNodeId] = useState("");
  const [lookedUpNode, setLookedUpNode] = useState(null);

  const handleRegisterNode = async () => {
    setRegistrationLoading(true);

    try {
      const response = await NodeAPI.registerNode();
      setRegisteredNode(response.data.data);
    } catch (error) {
      alert("Failed to register node");
    }

    setRegistrationLoading(false);
  };

  const handleCheckHealth = async () => {
    setHealthLoading(true);

    try {
      const response = await NodeAPI.checkHealth();
      setHealthStatus(response.data);
    } catch (error) {
      alert("Server not reachable");
    }

    setHealthLoading(false);
  };

  const handleLookupNode = async () => {
    try {
      const response = await NodeAPI.getNode(lookupNodeId);
      setLookedUpNode(response.data.data);
    } catch (error) {
      alert("Node not found");
    }
  };

  return (
    <div className="app-container">

      <header className="app-header">
        <h1>🔗 Relay Node Dashboard</h1>
        <p>Blockchain-Based Reputation-Aware Relay Selection</p>
      </header>

      {/* TAB NAVIGATION */}

      <div className="tabs">

        <button onClick={() => setActiveTab("dashboard")}>
          Dashboard
        </button>

        <button onClick={() => setActiveTab("nodes")}>
          Nodes
        </button>

        <button onClick={() => setActiveTab("relays")}>
          Relays
        </button>

        <button onClick={() => setActiveTab("messages")}>
          Messages
        </button>

        <button onClick={() => setActiveTab("monitoring")}>
          Monitoring
        </button>

      </div>

      {/* DASHBOARD TAB */}

      {activeTab === "dashboard" && (

        <div className="app-main">

          {/* SERVER HEALTH */}

          <section className="card">

            <div className="card-header">
              <h2>🏥 Server Health</h2>
            </div>

            <div className="card-body">

              <button
                className="btn btn-primary"
                onClick={handleCheckHealth}
                disabled={healthLoading}
              >
                {healthLoading ? "Checking..." : "Check Server Health"}
              </button>

              {healthStatus && (
                <p>Status: {healthStatus.status}</p>
              )}

            </div>

          </section>

          {/* REGISTER NODE */}

          <section className="card">

            <div className="card-header">
              <h2>➕ Register Node</h2>
            </div>

            <div className="card-body">

              <button
                className="btn btn-success"
                onClick={handleRegisterNode}
                disabled={registrationLoading}
              >
                {registrationLoading ? "Registering..." : "Register Node"}
              </button>

              {registeredNode && (
                <div className="result-container success">

                  <p>
                    <strong>Node ID:</strong>
                  </p>

                  <code>
                    {registeredNode.nodeId}
                  </code>

                </div>
              )}

            </div>

          </section>

          {/* NODE LOOKUP */}

          <section className="card">

            <div className="card-header">
              <h2>🔍 Node Lookup</h2>
            </div>

            <div className="card-body">

              <input
                className="input-field"
                placeholder="Enter Node ID"
                value={lookupNodeId}
                onChange={(e) => setLookupNodeId(e.target.value)}
              />

              <button
                className="btn btn-info"
                onClick={handleLookupNode}
              >
                Search
              </button>

              {lookedUpNode && (
                <div className="result-container success">

                  <p>
                    Trust Score: {lookedUpNode.trustScore}
                  </p>

                  <p>
                    Malicious: {lookedUpNode.malicious ? "Yes" : "No"}
                  </p>

                </div>
              )}

            </div>

          </section>

          <NetworkNodes />

          <RelayNodes />

          <RelaySelector />

        </div>

      )}

      {/* NODES TAB */}

      {activeTab === "nodes" && (

        <div className="app-main">

          <NetworkNodes />

        </div>

      )}

      {/* RELAYS TAB */}

      {activeTab === "relays" && (

        <div className="app-main">

          <RelayNodes />

          <RelaySelector />

          <RelayPerformance />

        </div>

      )}

      {/* MESSAGES TAB */}

      {activeTab === "messages" && (

        <div className="app-main">

          <MessageSender />

        </div>

      )}

      {/* MONITORING TAB */}

      {activeTab === "monitoring" && (

        <div className="app-main">

          <ReputationMonitor />

          <TrustScoreMonitor />

        </div>

      )}

    </div>
  );
}

export default App;