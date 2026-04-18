import React, { useRef, useEffect, useState, useCallback } from 'react';
import ForceGraph2D from 'react-force-graph-2d';

export default function NetworkFlowPanel({ activePath, relays, sourceNode, destNode }) {
  const fgRef = useRef();
  
  const [graphData, setGraphData] = useState({ nodes: [], links: [] });

  useEffect(() => {
    // Determine dynamic source and dest IDs, or fallback
    const srcId = sourceNode || 'Source';
    const dstId = destNode || 'Destination';

    const nodes = [
      { id: srcId, type: 'source', name: srcId, group: 1 },
      { id: dstId, type: 'dest', name: dstId, group: 3 }
    ];

    const links = [];

    // Add Relay Nodes
    relays.forEach(r => {
      // Don't add if it happens to be the source or dest node (prevent duplicate nodes)
      if (r.relayId !== srcId && r.relayId !== dstId) {
        nodes.push({
          id: r.relayId,
          type: r.status === 'BLOCKED' ? 'blocked' : 'relay',
          name: r.relayId.substring(0, 8),
          group: r.status === 'BLOCKED' ? 4 : 2,
          val: r.status === 'BLOCKED' ? 1.5 : (r.packetDeliveryRatio * 5) || 2
        });
      }
    });

    if (activePath && activePath.length > 0 && activePath[0] === 'DIRECT-P2P') {
       links.push({ source: srcId, target: dstId, isP2P: true, color: '#10b981', value: 3 });
    } else if (activePath && activePath.length > 0) {
       links.push({ source: srcId, target: activePath[0], isActive: true, color: '#6366f1' });
       
       for (let i = 0; i < activePath.length - 1; i++) {
         links.push({ source: activePath[i], target: activePath[i+1], isActive: true, color: '#6366f1' });
       }
       
       links.push({ source: activePath[activePath.length - 1], target: dstId, isActive: true, color: '#6366f1' });
    } else {
       relays.forEach(r => {
           if (r.status !== 'BLOCKED' && r.relayId !== srcId && r.relayId !== dstId) {
              links.push({ source: srcId, target: r.relayId, isBackground: true, color: 'rgba(255,255,255,0.05)' });
              links.push({ source: r.relayId, target: dstId, isBackground: true, color: 'rgba(255,255,255,0.05)' });
           }
       });
    }

    setGraphData({ nodes, links });
  }, [activePath, relays, sourceNode, destNode]);

  useEffect(() => {
    if (fgRef.current) {
      fgRef.current.d3Force('charge').strength(-250);
    }
  }, []);

  return (
    <div className="bg-gray-900 border border-purple-500/20 p-6 rounded-2xl shadow-xl transition-all duration-300">
      <h2 className="text-xl font-bold text-white mb-4">
        Network Flow
      </h2>
      <div className="h-64 sm:h-96 w-full rounded-xl overflow-hidden glass border border-purple-500/20">
        <ForceGraph2D
          ref={fgRef}
          graphData={graphData}
          width={800}
          height={400}
          nodeLabel="name"
          nodeColor={node => {
            if (node.type === 'source') return '#10b981'; // Green
            if (node.type === 'dest') return '#a855f7'; // Purple
            if (node.type === 'blocked') return '#ef4444'; // Red
            return '#3b82f6'; // Blue relay
          }}
          nodeRelSize={6}
          linkColor={link => link.color}
          linkWidth={link => link.isActive || link.isP2P ? 3 : 1}
          linkDirectionalParticles={link => link.isActive || link.isP2P ? 4 : 0}
          linkDirectionalParticleSpeed={d => d.value * 0.01 || 0.01}
          backgroundColor="#111827"
        />
      </div>
      <div className="mt-4 flex flex-wrap gap-4 text-xs justify-center text-gray-300 font-medium">
        <div className="flex items-center"><span className="inline-block w-3 h-3 rounded-full bg-emerald-500 mr-2"></span> Source Node</div>
        <div className="flex items-center"><span className="inline-block w-3 h-3 rounded-full bg-blue-500 mr-2"></span> Relay Node</div>
        <div className="flex items-center"><span className="inline-block w-3 h-3 rounded-full bg-red-500 mr-2"></span> Blocked Node</div>
        <div className="flex items-center"><span className="inline-block w-3 h-3 rounded-full bg-purple-500 mr-2"></span> Dest Node</div>
      </div>
    </div>
  );
}
