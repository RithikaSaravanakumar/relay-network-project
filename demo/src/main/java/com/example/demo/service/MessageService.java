package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.Message;
import com.example.demo.model.RelayNode;
import com.example.demo.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RelaySelectionService relaySelectionService;
    private final RelayNodeService relayNodeService;
    private final BlockchainService blockchainService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(MessageRepository messageRepository,
                          RelaySelectionService relaySelectionService,
                          RelayNodeService relayNodeService,
                          BlockchainService blockchainService,
                          SimpMessagingTemplate messagingTemplate) {

        this.messageRepository = messageRepository;
        this.relaySelectionService = relaySelectionService;
        this.relayNodeService = relayNodeService;
        this.blockchainService = blockchainService;
        this.messagingTemplate = messagingTemplate;
    }

    public Message sendMessage(String sender, String receiver, String content, String userId) {
        System.out.println("Processing message for user " + userId + ": " + sender + " -> " + receiver);

        // Ensure nodes exist in registry
        relayNodeService.getOrCreateRelay(sender, userId);
        relayNodeService.getOrCreateRelay(receiver, userId);

        Message message = new Message();
        message.setMessageId(UUID.randomUUID().toString());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setUserId(userId);
        message.setTimestamp(System.currentTimeMillis());

        // 1. SIMULATE P2P DIRECT CONNECTION
        boolean isDirectP2P = Math.random() > 0.6; // 40% success rate due to NAT

        if (isDirectP2P) {
            System.out.println("✅ P2P Direct Connection Successful!");
            message.setHopPath(new ArrayList<>());
            message.setHopLatencies(new ArrayList<>());
            message.setHopCount(0);
            message.setLatencyMs(10 + (long)(Math.random() * 20)); // ultra low latency
            message.setDelivered(true);
            message.setRelayNode("DIRECT-P2P");
            
            Message saved = messageRepository.save(message);
            broadcastUpdates(saved);
            return saved;
        }

        System.out.println("⚠️ P2P Connection Failed (NAT/Firewall). Falling back to Relay Network...");

        // 2. FALLBACK TO MULTI-HOP RELAY
        List<RelayNode> path = relaySelectionService.selectMultiHopPath(2, userId);
        
        if (path.isEmpty()) {
            throw new RuntimeException("No active relays available for fallback routing");
        }

        boolean entirePathSuccess = true;
        long totalLatencyMs = 0;
        List<String> hopIds = new ArrayList<>();
        List<Long> hopLats = new ArrayList<>();

        for (RelayNode relay : path) {
            hopIds.add(relay.getRelayId());

            long hopLatency = 15 + (long)(Math.random() * 80); // Increased range to allow > 70 condition
            totalLatencyMs += hopLatency;
            hopLats.add(hopLatency);

            // --- REALISTIC DELIVERY LOGIC ---
            double trust = relay.getTrust(); // 0 to 1
            double successProbability = 0.5 + (0.4 * trust);
            boolean hopSuccess = Math.random() < successProbability;

            // Failure condition for high latency
            if (hopLatency > 70) {
                hopSuccess = Math.random() < 0.6;
            }

            // Random failure chance (15%)
            if (Math.random() < 0.15) {
                hopSuccess = false;
            }

            System.out.println("Hop result for " + relay.getRelayId() + " (Trust: " + trust + ", Latency: " + hopLatency + "): " + hopSuccess);

            if (!hopSuccess) {
                entirePathSuccess = false;
            }

            updateRelayMetrics(relay, hopSuccess, hopLatency);
        }

        message.setHopPath(hopIds);
        message.setHopLatencies(hopLats);
        message.setHopCount(hopIds.size());
        message.setLatencyMs(totalLatencyMs);
        message.setDelivered(entirePathSuccess);
        
        // Ensure legacy relayNode field has the first hop for compatibility
        message.setRelayNode(hopIds.get(0));

        Message saved = messageRepository.save(message);
        broadcastUpdates(saved);
        
        return saved;
    }

    private void updateRelayMetrics(RelayNode relay, boolean success, long latency) {
        int total = relay.getTotalRequests() == null ? 0 : relay.getTotalRequests();
        int successCount = relay.getSuccessCount() == null ? 0 : relay.getSuccessCount();
        int failureCount = relay.getFailureCount() == null ? 0 : relay.getFailureCount();
        double currentTrust = relay.getTrust();

        total++;
        if (success) {
            successCount++;
            currentTrust += 0.02;
        } else {
            failureCount++;
            currentTrust -= 0.05;
        }

        // Clamp trust between 0 and 1
        currentTrust = Math.max(0.0, Math.min(1.0, currentTrust));

        double pdr = (double) successCount / total;
        double currentFailureRate = (double) failureCount / total;

        relay.setTotalRequests(total);
        relay.setSuccessCount(successCount);
        relay.setFailureCount(failureCount);
        relay.setTrust(currentTrust);
        relay.setPacketDeliveryRatio(pdr);
        relay.setFailureRate(currentFailureRate);
        relay.setLatency((double) latency);

        // Security: Relay Blocking
        if (currentFailureRate > 0.49 || currentTrust < 0.2) {
            System.out.println("🚨 RELAY BLOCKED: " + relay.getRelayId() + " (Trust: " + currentTrust + ")");
            relay.setStatus("BLOCKED");
        } else {
            relay.setStatus("ACTIVE");
        }

        relayNodeService.save(relay);

        try {
            blockchainService.updateReputation(relay.getRelayId(), currentTrust);
        } catch (Exception e) {
            System.out.println("Blockchain write failed: " + e.getMessage());
        }
    }

    private void broadcastUpdates(Message message) {
        // Broadcast new message event
        messagingTemplate.convertAndSend("/topic/messages", message);
        
        // Broadcast metric updates (leaderboard refresh)
        messagingTemplate.convertAndSend("/topic/metrics", relayNodeService.listRelays(message.getUserId()));
    }

    public List<Message> listMessages(String userId) {
        return messageRepository.findAllByUserId(userId);
    }
}