package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;

@Service
public class RelayPerformanceService {

    private final RelayNodeService relayNodeService;

    public RelayPerformanceService(RelayNodeService relayNodeService) {
        this.relayNodeService = relayNodeService;
    }

    public RelayNode updatePerformanceMetrics(String relayId, Double latency, Double packetDeliveryRatio, Double failureRate) {
        RelayNode relay = relayNodeService.findByRelayId(relayId);
        if (relay == null) {
            return null;
        }
        if (latency != null) relay.setLatency(latency);
        if (packetDeliveryRatio != null) relay.setPacketDeliveryRatio(packetDeliveryRatio);
        if (failureRate != null) relay.setFailureRate(failureRate);
        return relayNodeService.save(relay);
    }

}
