package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Node;
import com.example.demo.model.RelayNode;
import com.example.demo.repository.NodeRepository;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class ReputationService {

    private final RelayNodeRepository relayNodeRepository;
    private final NodeRepository nodeRepository;
    private final BlockchainService blockchainService;

    public ReputationService(RelayNodeRepository relayNodeRepository, NodeRepository nodeRepository, BlockchainService blockchainService) {
        this.relayNodeRepository = relayNodeRepository;
        this.nodeRepository = nodeRepository;
        this.blockchainService = blockchainService;
    }

    public double calculateReputationScore(String nodeId) {
        List<RelayNode> relays = relayNodeRepository.findByNodeId(nodeId);
        if (relays == null || relays.isEmpty()) {
            return 0.0;
        }

        double avgPdr = relays.stream().filter(r -> r.getPacketDeliveryRatio() != null).mapToDouble(RelayNode::getPacketDeliveryRatio).average().orElse(0.0);
        double avgLatency = relays.stream().filter(r -> r.getLatency() != null).mapToDouble(RelayNode::getLatency).average().orElse(0.0);
        double avgFailure = relays.stream().filter(r -> r.getFailureRate() != null).mapToDouble(RelayNode::getFailureRate).average().orElse(0.0);

        double latencyScore = Math.max(0.0, 1.0 - (avgLatency / 1000.0));
        double reliability = Math.max(0.0, 1.0 - avgFailure);

        double reputationScore = 0.5 * avgPdr + 0.3 * latencyScore + 0.2 * reliability;

        // round to 4 decimal places
        BigDecimal bd = BigDecimal.valueOf(reputationScore).setScale(4, RoundingMode.HALF_UP);
        reputationScore = bd.doubleValue();

        // update Node trustScore (0-100 scale)
        nodeRepository.findByNodeId(nodeId).ifPresent(node -> {
            int trust = (int) Math.round(reputationScore * 100);
            node.setTrustScore(trust);
            nodeRepository.save(node);
        });

        // push to blockchain ledger
        try {
            blockchainService.updateReputationOnBlockchain(nodeId, reputationScore);
        } catch (Exception ex) {
            // log but continue
            System.err.println("Failed to update reputation on blockchain: " + ex.getMessage());
        }

        return reputationScore;
    }

}
package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.model.Node;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

@Service
public class ReputationService {

    private final MessageRepository messageRepository;
    private final NodeRepository nodeRepository;
    private static final Logger log = LoggerFactory.getLogger(ReputationService.class);

    // Threshold for marking node as malicious if failure rate exceeds
    private static final double FAILURE_THRESHOLD = 0.5;

    public ReputationService(MessageRepository messageRepository, NodeRepository nodeRepository) {
        this.messageRepository = messageRepository;
        this.nodeRepository = nodeRepository;
    }

    /**
     * Recalculate and update the trust score for a node using message history.
     */
    public void updateTrustScore(String nodeId) {
        try {
            List<Message> sent = messageRepository.findBySourceNode(nodeId);

            double successRate = 1.0;
            if (!sent.isEmpty()) {
                long successes = sent.stream().filter(m -> Boolean.TRUE.equals(m.getDelivered())).count();
                successRate = (double) successes / (double) sent.size();
            }

            // Latency score: average latency normalized (assume 1000ms worst case)
            OptionalDouble avgLatency = sent.stream().filter(m -> m.getLatencyMs() != null)
                    .mapToLong(Message::getLatencyMs).average();
            double latencyScore = 1.0;
            if (avgLatency.isPresent()) {
                latencyScore = Math.max(0.0, 1.0 - (avgLatency.getAsDouble() / 1000.0));
            }

            // Reliability as relay: fraction of relay deliveries successful
            List<Message> relayed = messageRepository.findByRelayNode(nodeId);
            double reliability = 1.0;
            if (!relayed.isEmpty()) {
                long relaySuccess = relayed.stream().filter(m -> Boolean.TRUE.equals(m.getDelivered())).count();
                reliability = (double) relaySuccess / (double) relayed.size();
            }

            double trustScoreFloat = 0.5 * successRate + 0.3 * latencyScore + 0.2 * reliability;
            int trustScore = (int) Math.round(trustScoreFloat * 100.0);

            // Prepare final locals for lambda capture
            final int finalTrustScore = trustScore;
            final double finalFailureRate = 1.0 - successRate;

            // Update node record
            nodeRepository.findByNodeId(nodeId).ifPresent(node -> {
                node.setTrustScore(finalTrustScore);

                // Mark as malicious if failure rate exceeds threshold
                if (finalFailureRate > FAILURE_THRESHOLD) {
                    node.setMalicious(true);
                    log.warn("Node {} marked malicious (failureRate={})", nodeId, finalFailureRate);
                }

                nodeRepository.save(node);
            });

            log.info("Updated trust score for {}: {}", nodeId, trustScore);

        } catch (Exception e) {
            log.error("Failed to update trust score for {}: {}", nodeId, e.getMessage(), e);
        }
    }

}
