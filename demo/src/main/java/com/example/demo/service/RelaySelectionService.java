package com.example.demo.service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class RelaySelectionService {

    private final RelayNodeRepository relayRepository;
    private final Random random = new Random();

    public RelaySelectionService(RelayNodeRepository relayRepository) {
        this.relayRepository = relayRepository;
    }

    // =========================================================
    // 🔥 SELECT BEST RELAY (EPSILON-GREEDY: EXPLORE + EXPLOIT)
    // =========================================================
    public RelayNode selectBestRelay() {

        List<RelayNode> relays = relayRepository.findAll();

        if (relays == null || relays.isEmpty()) {
            System.out.println("No relay nodes available");
            return null;
        }

        List<RelayNode> activeRelays = relays.stream()
                .filter(r -> r != null && "ACTIVE".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());

        if (activeRelays.isEmpty()) {
            System.out.println("⚠️ No active relay nodes available");
            return null;
        }

        RelayNode selected;

        // 20% chance → random relay (exploration)
        if (Math.random() < 0.2) {
            selected = activeRelays.get(random.nextInt(activeRelays.size()));
            System.out.println("🎲 Using RANDOM relay (exploration): " + selected.getRelayId());
        } else {
            // 80% → best relay with slight jitter to break ties naturally
            selected = activeRelays.stream()
                    .max(Comparator.comparingDouble(
                            r -> calculateScore(r) + Math.random() * 0.05))
                    .orElse(activeRelays.get(0));
            System.out.println("🏆 Using BEST relay (exploitation): " + selected.getRelayId()
                    + " | Score: " + String.format("%.4f", calculateScore(selected)));
        }

        return selected;
    }

    // =========================================================
    // 🔥 TRUST SCORE CALCULATION (FINAL FIXED VERSION)
    // =========================================================
    private double calculateScore(RelayNode relay) {

        if (relay == null) return 0;

        double pdr = relay.getPacketDeliveryRatio() != null
                ? relay.getPacketDeliveryRatio()
                : 0.0;

        double latency = relay.getLatency() != null && relay.getLatency() > 0
                ? relay.getLatency()
                : 1.0;

        double failureRate = relay.getFailureRate() != null
                ? relay.getFailureRate()
                : 0.0;

        // 🔥 FINAL NORMALIZED TRUST FORMULA
        double trust =
                (0.5 * pdr) +
                (0.3 * (1.0 / (1 + latency))) +
                (0.2 * (1 - failureRate));

        // 🔥 SAFETY: keep trust in range [0,1]
        if (trust < 0) trust = 0;
        if (trust > 1) trust = 1;

        return trust;
    }
}