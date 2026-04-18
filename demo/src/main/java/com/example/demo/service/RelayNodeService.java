
// // // package com.example.demo.service;

// // // import java.util.List;
// // // import java.util.UUID;

// // // import org.springframework.stereotype.Service;

// // // import com.example.demo.model.RelayNode;
// // // import com.example.demo.repository.RelayNodeRepository;

// // // @Service
// // // public class RelayNodeService {

// // //     private final RelayNodeRepository relayRepository;

// // //     public RelayNodeService(RelayNodeRepository relayRepository) {
// // //         this.relayRepository = relayRepository;
// // //     }

// // //     /**
// // //      * Register a new relay node
// // //      */
// // //     public RelayNode registerRelay(RelayNode relay) {

// // //         String relayId = "relay-" + UUID.randomUUID().toString().substring(0, 8);
// // //         relay.setRelayId(relayId);

// // //         if (relay.getLatency() == null) {
// // //             relay.setLatency(50.0);
// // //         }

// // //         if (relay.getPacketDeliveryRatio() == null) {
// // //             relay.setPacketDeliveryRatio(0.95);
// // //         }

// // //         if (relay.getFailureRate() == null) {
// // //             relay.setFailureRate(0.02);
// // //         }

// // //         if (relay.getStatus() == null) {
// // //             relay.setStatus("ACTIVE");
// // //         }

// // //         relay.setCreatedAt(System.currentTimeMillis());

// // //         return relayRepository.save(relay);
// // //     }

// // //     /**
// // //      * Return all relay nodes
// // //      */
// // //     public List<RelayNode> listRelays() {
// // //         return relayRepository.findAll();
// // //     }

// // //     /**
// // //      * Find relay using relayId
// // //      */
// // //     public RelayNode findByRelayId(String relayId) {

// // //         return relayRepository.findByRelayId(relayId)
// // //                 .orElse(null);
// // //     }

// // //     /**
// // //      * Save relay (used by performance updates)
// // //      */
// // //     public RelayNode save(RelayNode relay) {
// // //         return relayRepository.save(relay);
// // //     }
// // // }
// // package com.example.demo.service;

// // import java.util.List;
// // import java.util.UUID;

// // import org.springframework.stereotype.Service;

// // import com.example.demo.model.RelayNode;
// // import com.example.demo.repository.RelayNodeRepository;

// // @Service
// // public class RelayNodeService {

// //     private final RelayNodeRepository relayRepository;

// //     public RelayNodeService(RelayNodeRepository relayRepository) {
// //         this.relayRepository = relayRepository;
// //     }

// //     /**
// //      * 🔥 AUTO CREATE RELAY (CORE FEATURE)
// //      */
// //     public RelayNode getOrCreateRelay(String nodeId) {

// //         List<RelayNode> relays = relayRepository.findAll();

// //         for (RelayNode r : relays) {
// //             if (r.getNodeId().equals(nodeId)) {
// //                 return r;
// //             }
// //         }

// //         // Create new relay automatically
// //         RelayNode relay = new RelayNode();

// //         relay.setRelayId("relay-" + UUID.randomUUID().toString().substring(0, 8));
// //         relay.setNodeId(nodeId);

// //         // Default values
// //         relay.setLatency(50.0);
// //         relay.setPacketDeliveryRatio(0.95);
// //         relay.setFailureRate(0.02);
// //         relay.setStatus("ACTIVE");

// //         relay.setTotalRequests(0);
// //         relay.setSuccessCount(0);
// //         relay.setFailureCount(0);

// //         relay.setCreatedAt(System.currentTimeMillis());

// //         return relayRepository.save(relay);
// //     }

// //     /**
// //      * Register relay manually (optional)
// //      */
// //     public RelayNode registerRelay(RelayNode relay) {

// //         String relayId = "relay-" + UUID.randomUUID().toString().substring(0, 8);
// //         relay.setRelayId(relayId);

// //         if (relay.getLatency() == null) {
// //             relay.setLatency(50.0);
// //         }

// //         if (relay.getPacketDeliveryRatio() == null) {
// //             relay.setPacketDeliveryRatio(0.95);
// //         }

// //         if (relay.getFailureRate() == null) {
// //             relay.setFailureRate(0.02);
// //         }

// //         if (relay.getStatus() == null) {
// //             relay.setStatus("ACTIVE");
// //         }

// //         relay.setCreatedAt(System.currentTimeMillis());

// //         return relayRepository.save(relay);
// //     }

// //     public List<RelayNode> listRelays() {
// //         return relayRepository.findAll();
// //     }

// //     public RelayNode findByRelayId(String relayId) {
// //         return relayRepository.findByRelayId(relayId).orElse(null);
// //     }

// //     public RelayNode save(RelayNode relay) {
// //         return relayRepository.save(relay);
// //     }
// // }
// package com.example.demo.service;

// import java.util.List;
// import java.util.UUID;

// import org.springframework.stereotype.Service;

// import com.example.demo.model.RelayNode;
// import com.example.demo.repository.RelayNodeRepository;

// @Service
// public class RelayNodeService {

//     private final RelayNodeRepository relayRepository;

//     public RelayNodeService(RelayNodeRepository relayRepository) {
//         this.relayRepository = relayRepository;
//     }

//     // 🔥 AUTO CREATE RELAY
//     public RelayNode getOrCreateRelay(String nodeId) {

//         List<RelayNode> relays = relayRepository.findAll();

//         for (RelayNode r : relays) {
//             if (r.getNodeId().equals(nodeId)) {
//                 return r;
//             }
//         }

//         RelayNode relay = new RelayNode();

//         relay.setRelayId("relay-" + UUID.randomUUID().toString().substring(0, 8));
//         relay.setNodeId(nodeId);

//         relay.setLatency(50.0);
//         relay.setPacketDeliveryRatio(0.95);
//         relay.setFailureRate(0.02);
//         relay.setStatus("ACTIVE");

//         relay.setCreatedAt(System.currentTimeMillis());

//         // 🔥 INITIALIZE METRICS
//         relay.setTotalRequests(0);
//         relay.setSuccessCount(0);
//         relay.setFailureCount(0);

//         return relayRepository.save(relay);
//     }

//     public RelayNode save(RelayNode relay) {
//         return relayRepository.save(relay);
//     }

//     public List<RelayNode> listRelays() {
//         return relayRepository.findAll();
//     }

//     public RelayNode findByRelayId(String relayId) {
//         return relayRepository.findByRelayId(relayId).orElse(null);
//     }
// }
package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class RelayNodeService {

    private final RelayNodeRepository relayRepository;
    private final BlockchainService blockchainService;

    public RelayNodeService(RelayNodeRepository relayRepository, BlockchainService blockchainService) {
        this.relayRepository = relayRepository;
        this.blockchainService = blockchainService;
    }

    private String generateSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().substring(0, 16); // 16 char ID for readability
        } catch (Exception e) {
            return UUID.randomUUID().toString().substring(0, 16);
        }
    }

    // =========================================================
    // 🔥 AUTO CREATE RELAY (USED IN MESSAGE FLOW)
    // =========================================================
    public RelayNode getOrCreateRelay(String nodeId, String userId) {

        List<RelayNode> relays = relayRepository.findAllByUserId(userId);

        for (RelayNode r : relays) {
            if (nodeId.equals(r.getNodeId())) {
                return r;
            }
        }

        // Create new relay automatically
        RelayNode relay = new RelayNode();

        String rawId = UUID.randomUUID().toString();
        relay.setRelayId("relay-" + generateSha256(rawId));
        relay.setNodeId(nodeId);
        relay.setPublicKey("PUB-" + UUID.randomUUID().toString().substring(0,12));
        relay.setUserId(userId);

        // Default performance values
        relay.setLatency(50.0);
        relay.setPacketDeliveryRatio(0.95);
        relay.setFailureRate(0.02);
        relay.setStatus("ACTIVE");

        relay.setCreatedAt(System.currentTimeMillis());

        // 🔥 INITIALIZE METRICS
        relay.setTotalRequests(0);
        relay.setSuccessCount(0);
        relay.setFailureCount(0);

        RelayNode saved = relayRepository.save(relay);
        blockchainService.registerNodeOnBlockchain(saved.getRelayId(), saved.getPublicKey());
        return saved;
    }

    // =========================================================
    // 🔥 MANUAL CREATE RELAY (USED BY UI BUTTON)
    // =========================================================
    public RelayNode registerRelay(RelayNode relay, String userId) {

        String rawId = UUID.randomUUID().toString();
        relay.setRelayId("relay-" + generateSha256(rawId));
        relay.setUserId(userId);
        
        if (relay.getPublicKey() == null || relay.getPublicKey().isEmpty()) {
            relay.setPublicKey("PUB-" + UUID.randomUUID().toString().substring(0,12));
        }

        if (relay.getLatency() == null) {
            relay.setLatency(50.0);
        }

        if (relay.getPacketDeliveryRatio() == null) {
            relay.setPacketDeliveryRatio(0.95);
        }

        if (relay.getFailureRate() == null) {
            relay.setFailureRate(0.02);
        }

        if (relay.getStatus() == null) {
            relay.setStatus("ACTIVE");
        }

        relay.setCreatedAt(System.currentTimeMillis());

        // 🔥 INITIALIZE METRICS (IMPORTANT)
        relay.setTotalRequests(0);
        relay.setSuccessCount(0);
        relay.setFailureCount(0);

        RelayNode saved = relayRepository.save(relay);
        blockchainService.registerNodeOnBlockchain(saved.getRelayId(), saved.getPublicKey());
        return saved;
    }

    // =========================================================
    // 🔹 SAVE RELAY
    // =========================================================
    public RelayNode save(RelayNode relay) {
        return relayRepository.save(relay);
    }

    // =========================================================
    // 🔹 GET ALL RELAYS
    // =========================================================
    public List<RelayNode> listRelays(String userId) {
        return relayRepository.findAllByUserId(userId);
    }

    // =========================================================
    // 🔹 FIND BY RELAY ID
    // =========================================================
    public RelayNode findByRelayId(String relayId) {
        return relayRepository.findByRelayId(relayId).orElse(null);
    }
}