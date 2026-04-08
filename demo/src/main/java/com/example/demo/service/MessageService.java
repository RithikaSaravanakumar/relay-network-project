
// // // // package com.example.demo.service;

// // // // import java.util.UUID;

// // // // import org.springframework.stereotype.Service;

// // // // import com.example.demo.model.Message;
// // // // import com.example.demo.model.RelayNode;
// // // // import com.example.demo.repository.MessageRepository;

// // // // @Service
// // // // public class MessageService {

// // // //     private final MessageRepository messageRepository;
// // // //     private final RelaySelectionService relaySelectionService;
// // // //     private final ReputationService reputationService;

// // // //     public MessageService(MessageRepository messageRepository,
// // // //                           RelaySelectionService relaySelectionService,
// // // //                           ReputationService reputationService) {

// // // //         this.messageRepository = messageRepository;
// // // //         this.relaySelectionService = relaySelectionService;
// // // //         this.reputationService = reputationService;
// // // //     }

// // // //     public Message sendMessage(String sender,
// // // //                                String receiver,
// // // //                                String content) {

// // // //         RelayNode relay = relaySelectionService.selectBestRelay();

// // // //         Message message = new Message();

// // // //         message.setMessageId(UUID.randomUUID().toString());

// // // //         message.setSender(sender);
// // // //         message.setReceiver(receiver);

// // // //         message.setRelayNode(relay.getNodeId());

// // // //         long latency = (long)(relay.getLatency() + (Math.random() * 50));

// // // //         message.setLatencyMs(latency);
// // // //         message.setDelivered(true);

// // // //         message.setTimestamp(System.currentTimeMillis());

// // // //         Message saved = messageRepository.save(message);

// // // //         reputationService.calculateReputationScore(relay.getNodeId());

// // // //         return saved;
// // // //     }
// // // // }
// // // package com.example.demo.service;

// // // import java.util.UUID;

// // // import org.springframework.stereotype.Service;

// // // import com.example.demo.model.Message;
// // // import com.example.demo.model.RelayNode;
// // // import com.example.demo.repository.MessageRepository;

// // // @Service
// // // public class MessageService {

// // //     private final MessageRepository messageRepository;
// // //     private final RelaySelectionService relaySelectionService;
// // //     private final ReputationService reputationService;
// // //     private final RelayNodeService relayNodeService;

// // //     public MessageService(MessageRepository messageRepository,
// // //                           RelaySelectionService relaySelectionService,
// // //                           ReputationService reputationService,
// // //                           RelayNodeService relayNodeService) {

// // //         this.messageRepository = messageRepository;
// // //         this.relaySelectionService = relaySelectionService;
// // //         this.reputationService = reputationService;
// // //         this.relayNodeService = relayNodeService;
// // //     }

// // //     public Message sendMessage(String sender,
// // //                                String receiver,
// // //                                String content) {

// // //         // ✅ STEP 1: Auto create relays if not exist
// // //         relayNodeService.getOrCreateRelay(sender);
// // //         relayNodeService.getOrCreateRelay(receiver);

// // //         // ✅ STEP 2: Select best relay
// // //         RelayNode relay = relaySelectionService.selectBestRelay();

// // //         if (relay == null) {
// // //             throw new RuntimeException("No active relays available");
// // //         }

// // //         // ✅ STEP 3: Measure latency
// // //         long start = System.currentTimeMillis();

// // //         // ✅ STEP 4: Simulate success/failure (90% success)
// // //         boolean success = Math.random() > 0.1;

// // //         long latency = System.currentTimeMillis() - start + (long)(Math.random() * 50);

// // //         // ✅ STEP 5: Update relay metrics
// // //         int total = relay.getTotalRequests() == null ? 0 : relay.getTotalRequests();
// // //         int successCount = relay.getSuccessCount() == null ? 0 : relay.getSuccessCount();
// // //         int failureCount = relay.getFailureCount() == null ? 0 : relay.getFailureCount();

// // //         total++;

// // //         if (success) {
// // //             successCount++;
// // //         } else {
// // //             failureCount++;
// // //         }

// // //         double pdr = (double) successCount / total;
// // //         double failureRate = (double) failureCount / total;

// // //         relay.setTotalRequests(total);
// // //         relay.setSuccessCount(successCount);
// // //         relay.setFailureCount(failureCount);

// // //         relay.setPacketDeliveryRatio(pdr);
// // //         relay.setFailureRate(failureRate);
// // //         relay.setLatency((double) latency);

// // //         // ✅ Auto deactivate bad relays
// // //         if (failureRate > 0.5) {
// // //             relay.setStatus("INACTIVE");
// // //         }

// // //         relayNodeService.save(relay);

// // //         // ✅ STEP 6: Save message
// // //         Message message = new Message();

// // //         message.setMessageId(UUID.randomUUID().toString());
// // //         message.setSender(sender);
// // //         message.setReceiver(receiver);
// // //         message.setRelayNode(relay.getNodeId());
// // //         message.setLatencyMs(latency);
// // //         message.setDelivered(success);
// // //         message.setTimestamp(System.currentTimeMillis());

// // //         Message saved = messageRepository.save(message);

// // //         // ✅ STEP 7: Update reputation (Blockchain)
// // //         reputationService.calculateReputationScore(relay.getNodeId());

// // //         return saved;
// // //     }
// // // }
// // package com.example.demo.service;

// // import java.util.UUID;

// // import org.springframework.stereotype.Service;

// // import com.example.demo.model.Message;
// // import com.example.demo.model.RelayNode;
// // import com.example.demo.repository.MessageRepository;

// // @Service
// // public class MessageService {

// //     private final MessageRepository messageRepository;
// //     private final RelaySelectionService relaySelectionService;
// //     private final ReputationService reputationService;
// //     private final RelayNodeService relayNodeService;

// //     public MessageService(MessageRepository messageRepository,
// //                           RelaySelectionService relaySelectionService,
// //                           ReputationService reputationService,
// //                           RelayNodeService relayNodeService) {

// //         this.messageRepository = messageRepository;
// //         this.relaySelectionService = relaySelectionService;
// //         this.reputationService = reputationService;
// //         this.relayNodeService = relayNodeService;
// //     }

// //     public Message sendMessage(String sender,
// //                                String receiver,
// //                                String content) {

// //         // ✅ AUTO CREATE RELAYS
// //         relayNodeService.getOrCreateRelay(sender);
// //         relayNodeService.getOrCreateRelay(receiver);

// //         // ✅ SELECT BEST RELAY
// //         RelayNode relay = relaySelectionService.selectBestRelay();

// //         if (relay == null) {
// //             throw new RuntimeException("No active relays available");
// //         }

// //         // ✅ LATENCY TRACK
// //         long start = System.currentTimeMillis();

// //         boolean success = Math.random() > 0.1;

// //         long latency = System.currentTimeMillis() - start + (long)(Math.random() * 50);

// //         // ✅ SAFE METRIC FETCH
// //         int total = relay.getTotalRequests() == null ? 0 : relay.getTotalRequests();
// //         int successCount = relay.getSuccessCount() == null ? 0 : relay.getSuccessCount();
// //         int failureCount = relay.getFailureCount() == null ? 0 : relay.getFailureCount();

// //         total++;

// //         if (success) {
// //             successCount++;
// //         } else {
// //             failureCount++;
// //         }

// //         double pdr = (double) successCount / total;
// //         double failureRate = (double) failureCount / total;

// //         // ✅ UPDATE METRICS
// //         relay.setTotalRequests(total);
// //         relay.setSuccessCount(successCount);
// //         relay.setFailureCount(failureCount);

// //         relay.setPacketDeliveryRatio(pdr);
// //         relay.setFailureRate(failureRate);
// //         relay.setLatency((double) latency);

// //         if (failureRate > 0.5) {
// //             relay.setStatus("INACTIVE");
// //         }

// //         relayNodeService.save(relay);

// //         // ✅ SAVE MESSAGE
// //         Message message = new Message();

// //         message.setMessageId(UUID.randomUUID().toString());
// //         message.setSender(sender);
// //         message.setReceiver(receiver);
// //         message.setRelayNode(relay.getNodeId());
// //         message.setLatencyMs(latency);
// //         message.setDelivered(success);
// //         message.setTimestamp(System.currentTimeMillis());

// //         Message saved = messageRepository.save(message);

// //         // ✅ UPDATE REPUTATION (BLOCKCHAIN)
// //         reputationService.calculateReputationScore(relay.getNodeId());

// //         return saved;
// //     }
// // }
// package com.example.demo.service;

// import java.util.UUID;

// import org.springframework.stereotype.Service;

// import com.example.demo.model.Message;
// import com.example.demo.model.RelayNode;
// import com.example.demo.repository.MessageRepository;

// @Service
// public class MessageService {

//     private final MessageRepository messageRepository;
//     private final RelaySelectionService relaySelectionService;
//     private final RelayNodeService relayNodeService;
//     private final BlockchainService blockchainService;

//     public MessageService(MessageRepository messageRepository,
//                           RelaySelectionService relaySelectionService,
//                           RelayNodeService relayNodeService,
//                           BlockchainService blockchainService) {

//         this.messageRepository = messageRepository;
//         this.relaySelectionService = relaySelectionService;
//         this.relayNodeService = relayNodeService;
//         this.blockchainService = blockchainService;
//     }

//     public Message sendMessage(String sender,
//                                String receiver,
//                                String content) {

//         // ✅ AUTO CREATE RELAYS
//         relayNodeService.getOrCreateRelay(sender);
//         relayNodeService.getOrCreateRelay(receiver);

//         // ✅ SELECT BEST RELAY
//         RelayNode relay = relaySelectionService.selectBestRelay();

//         if (relay == null) {
//             throw new RuntimeException("No active relays available");
//         }

//         // ✅ LATENCY TRACK
//         long start = System.currentTimeMillis();

//         boolean success = Math.random() > 0.1;

//         long latency = System.currentTimeMillis() - start + (long)(Math.random() * 50);

//         // ✅ METRICS
//         int total = relay.getTotalRequests() == null ? 0 : relay.getTotalRequests();
//         int successCount = relay.getSuccessCount() == null ? 0 : relay.getSuccessCount();
//         int failureCount = relay.getFailureCount() == null ? 0 : relay.getFailureCount();

//         total++;

//         if (success) successCount++;
//         else failureCount++;

//         double pdr = (double) successCount / total;
//         double failureRate = (double) failureCount / total;

//         relay.setTotalRequests(total);
//         relay.setSuccessCount(successCount);
//         relay.setFailureCount(failureCount);

//         relay.setPacketDeliveryRatio(pdr);
//         relay.setFailureRate(failureRate);
//         relay.setLatency((double) latency);

//         if (failureRate > 0.5) {
//             relay.setStatus("INACTIVE");
//         }

//         relayNodeService.save(relay);

//         // ✅ TRUST CALCULATION (SAME AS SELECTION LOGIC)
//         double trust =
//                 (0.5 * pdr) +
//                 (0.3 * (1.0 / (1 + latency))) +
//                 (0.2 * (1 - failureRate));

//         // 🔥 STORE IN BLOCKCHAIN USING RELAY ID
//         try {
//         blockchainService.updateReputation(relay.getRelayId(), trust);
//         } catch (Exception e) {
//         System.out.println("Blockchain error: " + e.getMessage());
// }

//         // 🔥 FETCH BACK FROM BLOCKCHAIN
//         double blockchainTrust = 0;

//         try {
//         blockchainTrust = blockchainService.getReputation(relay.getRelayId());
//         } catch (Exception e) {
//         System.out.println("Blockchain read error: " + e.getMessage());
//         }
        
//         // ✅ SAVE MESSAGE
//         Message message = new Message();

//         message.setMessageId(UUID.randomUUID().toString());
//         message.setSender(sender);
//         message.setReceiver(receiver);
//         message.setRelayNode(relay.getRelayId()); // ✅ IMPORTANT
//         message.setLatencyMs(latency);
//         message.setDelivered(success);
//         message.setTimestamp(System.currentTimeMillis());

//         Message saved = messageRepository.save(message);

//         return saved;
//     }
// }
package com.example.demo.service;

import java.util.UUID;

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

    public MessageService(MessageRepository messageRepository,
                          RelaySelectionService relaySelectionService,
                          RelayNodeService relayNodeService,
                          BlockchainService blockchainService) {

        this.messageRepository = messageRepository;
        this.relaySelectionService = relaySelectionService;
        this.relayNodeService = relayNodeService;
        this.blockchainService = blockchainService;
    }

    public Message sendMessage(String sender, String receiver, String content) {

        System.out.println("Sending message from " + sender + " to " + receiver);

        // ✅ Auto create relays
        relayNodeService.getOrCreateRelay(sender);
        relayNodeService.getOrCreateRelay(receiver);

        // ✅ Select best relay
        RelayNode relay = relaySelectionService.selectBestRelay();

        if (relay == null) {
            throw new RuntimeException("No active relays available");
        }

        // ✅ Simulate latency + success
        long start = System.currentTimeMillis();
        boolean success = Math.random() > 0.1;
        long latency = System.currentTimeMillis() - start + (long)(Math.random() * 50);

        // ✅ Metrics
        int total = relay.getTotalRequests() == null ? 0 : relay.getTotalRequests();
        int successCount = relay.getSuccessCount() == null ? 0 : relay.getSuccessCount();
        int failureCount = relay.getFailureCount() == null ? 0 : relay.getFailureCount();

        total++;

        if (success) successCount++;
        else failureCount++;

        double pdr = (double) successCount / total;
        double failureRate = (double) failureCount / total;

        relay.setTotalRequests(total);
        relay.setSuccessCount(successCount);
        relay.setFailureCount(failureCount);

        relay.setPacketDeliveryRatio(pdr);
        relay.setFailureRate(failureRate);
        relay.setLatency((double) latency);

        if (failureRate > 0.5) {
            relay.setStatus("INACTIVE");
        }

        relayNodeService.save(relay);

        // ✅ Trust calculation (FIXED)
        double trust =
                (0.5 * pdr) +
                (0.3 * (1.0 / (1 + latency))) +
                (0.2 * (1 - failureRate));

        System.out.println("Calculated trust: " + trust);

        // 🔥 Safe blockchain write
        try {
            blockchainService.updateReputation(relay.getRelayId(), trust);
        } catch (Exception e) {
            System.out.println("Blockchain write failed: " + e.getMessage());
        }

        // 🔥 Safe blockchain read
        double blockchainTrust = trust;

        try {
            double value = blockchainService.getReputation(relay.getRelayId());
            if (value > 0) {
                blockchainTrust = value;
            }
        } catch (Exception e) {
            System.out.println("Blockchain read failed: " + e.getMessage());
        }

        // ✅ Save message
        Message message = new Message();

        message.setMessageId(UUID.randomUUID().toString());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setRelayNode(relay.getRelayId());
        message.setLatencyMs(latency);
        message.setDelivered(success);
        message.setTimestamp(System.currentTimeMillis());

        Message saved = messageRepository.save(message);

        System.out.println("Message sent via relay: " + relay.getRelayId());

        return saved;
    }
}