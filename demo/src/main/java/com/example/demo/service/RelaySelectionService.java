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
    public RelayNode selectBestRelay(String userId) {

        List<RelayNode> relays = relayRepository.findAllByUserId(userId);

        if (relays == null || relays.isEmpty()) {
            System.out.println("No relay nodes available for user: " + userId);
            return null;
        }

        List<RelayNode> activeRelays = relays.stream()
                .filter(r -> r != null && "ACTIVE".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());

        if (activeRelays.isEmpty()) {
            System.out.println("⚠️ No active relay nodes available for user: " + userId);
            return null;
        }

        RelayNode selected;

        // 10% chance → random relay (exploration)
        if (Math.random() < 0.1) {
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

    public List<RelayNode> selectMultiHopPath(int maxHops, String userId) {
        List<RelayNode> relays = relayRepository.findAllByUserId(userId);
        
        if (relays == null || relays.isEmpty()) {
            return List.of();
        }

        List<RelayNode> activeRelays = relays.stream()
                .filter(r -> r != null && !"BLOCKED".equalsIgnoreCase(r.getStatus()))
                .sorted(Comparator.comparingDouble(r -> -calculateScore(r)))
                .collect(Collectors.toList());

        if (activeRelays.isEmpty()) {
            return List.of();
        }

        int hops = Math.min(maxHops, activeRelays.size());
        
        // Return top N relays
        return activeRelays.subList(0, hops);
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
                : 100.0;

        double failureRate = relay.getFailureRate() != null
                ? relay.getFailureRate()
                : 0.0;
                
        // Fallback for trust (if not passed from blockchain) uses local PDR logic initially
        double trust = (0.5 * pdr) + (0.5 * (1 - failureRate));

        // 🔥 FINAL FORMULA STRICTLY AS REQUESTED
        // Success Rate is mapped to packetDeliveryRatio (PDR)
        // Latency is normalized (latency / 100) to keep it mostly [0,1]
        double score =
                (0.5 * trust) +
                (0.3 * pdr) -
                (0.2 * (latency / 100.0));

        // 🔥 SAFETY: keep score in range [0,1]
        if (score < 0) score = 0;
        if (score > 1) score = 1;

        return score;
    }
}