package com.example.demo.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.model.Message;
import com.example.demo.model.RelayNode;
import com.example.demo.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RelaySelectionService relaySelectionService;

    public MessageService(MessageRepository messageRepository, RelaySelectionService relaySelectionService) {
        this.messageRepository = messageRepository;
        this.relaySelectionService = relaySelectionService;
    }

    public Message sendMessage(Message message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setTimestamp(Instant.now().toEpochMilli());
        // choose relay if not provided
        if (message.getRelayNode() == null || message.getRelayNode().isEmpty()) {
            RelayNode selected = relaySelectionService.selectTrustedRelay().orElse(null);
            if (selected != null) message.setRelayNode(selected.getRelayId());
        }

        // simulate delivery
        message.setDelivered(true);
        message.setLatencyMs(50L);

        return messageRepository.save(message);
    }

    public List<Message> historyForNode(String nodeId) {
        if (nodeId == null) return messageRepository.findAll();
        List<Message> sent = messageRepository.findBySourceNode(nodeId);
        if (sent == null) return Collections.emptyList();
        return sent;
    }

}
package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.Message;
import com.example.demo.model.Node;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.NodeRepository;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final NodeRepository nodeRepository;
    private final RelaySelectionService relaySelectionService;
    private final ReputationService reputationService;

    public MessageService(MessageRepository messageRepository,
                          NodeRepository nodeRepository,
                          RelaySelectionService relaySelectionService,
                          ReputationService reputationService) {
        this.messageRepository = messageRepository;
        this.nodeRepository = nodeRepository;
        this.relaySelectionService = relaySelectionService;
        this.reputationService = reputationService;
    }

    /**
     * Simulate sending a message from source to destination. If direct send fails,
     * try using a relay node selected by RelaySelectionService.
     */
    public Message sendMessage(String sourceNodeId, String destinationNodeId, String content) throws Exception {
        // Validate nodes exist
        if (!nodeRepository.existsByNodeId(sourceNodeId)) {
            throw new Exception("Source node not found: " + sourceNodeId);
        }
        if (!nodeRepository.existsByNodeId(destinationNodeId)) {
            throw new Exception("Destination node not found: " + destinationNodeId);
        }

        // Simulate direct connection success probability (70%)
        boolean directSuccess = ThreadLocalRandom.current().nextInt(100) < 70;
        long baseLatency = ThreadLocalRandom.current().nextLong(30, 200); // ms

        Message message = Message.builder()
                .messageId(UUID.randomUUID().toString())
                .sourceNode(sourceNodeId)
                .destinationNode(destinationNodeId)
                .content(content)
                .timestamp(System.currentTimeMillis())
                .delivered(false)
                .latencyMs(baseLatency)
                .build();

        if (directSuccess) {
            message.setDelivered(true);
            message.setRelayNode(null);
            log.info("Direct delivery succeeded from {} to {}", sourceNodeId, destinationNodeId);

        } else {
            // Select best relay
            Optional<Node> relayOpt = relaySelectionService.selectBestRelayNode();
            if (relayOpt.isPresent()) {
                Node relay = relayOpt.get();
                // If the relay is the source or destination, treat as failure
                if (relay.getNodeId().equals(sourceNodeId) || relay.getNodeId().equals(destinationNodeId)) {
                    message.setDelivered(false);
                    message.setRelayNode(null);
                    log.warn("No suitable relay found for {} -> {}", sourceNodeId, destinationNodeId);
                } else {
                    // Relay will forward, add extra latency
                    long extra = ThreadLocalRandom.current().nextLong(20, 150);
                    message.setLatencyMs(message.getLatencyMs() + extra);
                    message.setRelayNode(relay.getNodeId());
                    message.setDelivered(true);
                    log.info("Delivered via relay {} for {} -> {}", relay.getNodeId(), sourceNodeId, destinationNodeId);
                }
            } else {
                message.setDelivered(false);
                message.setRelayNode(null);
                log.warn("No relay available for message {} -> {}", sourceNodeId, destinationNodeId);
            }
        }

        Message saved = messageRepository.save(message);

        // Update reputation for source and any relay involved
        try {
            reputationService.updateTrustScore(sourceNodeId);
            if (saved.getRelayNode() != null) {
                reputationService.updateTrustScore(saved.getRelayNode());
            }
        } catch (Exception e) {
            log.error("Error updating reputation after message send: {}", e.getMessage(), e);
        }

        return saved;
    }

}
