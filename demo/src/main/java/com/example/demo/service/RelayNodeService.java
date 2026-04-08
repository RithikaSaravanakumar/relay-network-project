
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

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class RelayNodeService {

    private final RelayNodeRepository relayRepository;

    public RelayNodeService(RelayNodeRepository relayRepository) {
        this.relayRepository = relayRepository;
    }

    // =========================================================
    // 🔥 AUTO CREATE RELAY (USED IN MESSAGE FLOW)
    // =========================================================
    public RelayNode getOrCreateRelay(String nodeId) {

        List<RelayNode> relays = relayRepository.findAll();

        for (RelayNode r : relays) {
            if (nodeId.equals(r.getNodeId())) {
                return r;
            }
        }

        // Create new relay automatically
        RelayNode relay = new RelayNode();

        relay.setRelayId("relay-" + UUID.randomUUID().toString().substring(0, 8));
        relay.setNodeId(nodeId);

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

        return relayRepository.save(relay);
    }

    // =========================================================
    // 🔥 MANUAL CREATE RELAY (USED BY UI BUTTON)
    // =========================================================
    public RelayNode registerRelay(RelayNode relay) {

        relay.setRelayId("relay-" + UUID.randomUUID().toString().substring(0, 8));

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

        return relayRepository.save(relay);
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
    public List<RelayNode> listRelays() {
        return relayRepository.findAll();
    }

    // =========================================================
    // 🔹 FIND BY RELAY ID
    // =========================================================
    public RelayNode findByRelayId(String relayId) {
        return relayRepository.findByRelayId(relayId).orElse(null);
    }
}