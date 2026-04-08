// package com.example.demo.service;

// import org.springframework.stereotype.Service;

// import com.example.demo.model.RelayNode;

// @Service
// public class RelayPerformanceService {

//     private final RelayNodeService relayNodeService;

//     public RelayPerformanceService(RelayNodeService relayNodeService) {
//         this.relayNodeService = relayNodeService;
//     }

//     public RelayNode updatePerformanceMetrics(String relayId, Double latency, Double packetDeliveryRatio, Double failureRate) {
//         RelayNode relay = relayNodeService.findByRelayId(relayId);
//         if (relay == null) {
//             return null;
//         }
//         if (latency != null) relay.setLatency(latency);
//         if (packetDeliveryRatio != null) relay.setPacketDeliveryRatio(packetDeliveryRatio);
//         if (failureRate != null) relay.setFailureRate(failureRate);
//         return relayNodeService.save(relay);
//     }

// }
package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;

@Service
public class RelayPerformanceService {

    private final RelayNodeService relayNodeService;
    private final ReputationService reputationService;

    public RelayPerformanceService(
            RelayNodeService relayNodeService,
            ReputationService reputationService) {

        this.relayNodeService = relayNodeService;
        this.reputationService = reputationService;
    }

    /**
     * Update relay performance metrics and automatically recalculate trust score
     */
    public RelayNode updatePerformanceMetrics(
            String relayId,
            Double latency,
            Double packetDeliveryRatio,
            Double failureRate) {

        RelayNode relay = relayNodeService.findByRelayId(relayId);

        if (relay == null) {
            return null;
        }

        // Update metrics if provided
        if (latency != null) {
            relay.setLatency(latency);
        }

        if (packetDeliveryRatio != null) {
            relay.setPacketDeliveryRatio(packetDeliveryRatio);
        }

        if (failureRate != null) {
            relay.setFailureRate(failureRate);
        }

        // Save updated relay
        RelayNode updatedRelay = relayNodeService.save(relay);

        // 🔹 Automatically update reputation + trust score
        try {
            reputationService.calculateReputationScore(updatedRelay.getNodeId());
        } catch (Exception e) {
            System.out.println("Failed to update reputation: " + e.getMessage());
        }

        return updatedRelay;
    }
}