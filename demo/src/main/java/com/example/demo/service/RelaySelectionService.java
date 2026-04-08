
// // package com.example.demo.service;

// // import java.util.Comparator;
// // import java.util.List;

// // import org.springframework.stereotype.Service;

// // import com.example.demo.model.RelayNode;
// // import com.example.demo.repository.RelayNodeRepository;

// // @Service
// // public class RelaySelectionService {

// //     private final RelayNodeRepository relayRepository;

// //     public RelaySelectionService(RelayNodeRepository relayRepository) {
// //         this.relayRepository = relayRepository;
// //     }

// //     public RelayNode selectBestRelay() {

// //         List<RelayNode> relays = relayRepository.findAll();

// //         return relays.stream()
// //                 .filter(r -> r.getStatus().equals("ACTIVE"))
// //                 .max(Comparator.comparingDouble(this::calculateScore))
// //                 .orElse(null);
// //     }

// //     private double calculateScore(RelayNode relay) {

// //         double pdrScore = relay.getPacketDeliveryRatio() * 50;

// //         double latencyScore = 50 - relay.getLatency();

// //         double failurePenalty = relay.getFailureRate() * 20;

// //         return pdrScore + latencyScore - failurePenalty;
// //     }
// // }
// package com.example.demo.service;

// import java.util.Comparator;
// import java.util.List;

// import org.springframework.stereotype.Service;

// import com.example.demo.model.RelayNode;
// import com.example.demo.repository.RelayNodeRepository;

// @Service
// public class RelaySelectionService {

//     private final RelayNodeRepository relayRepository;

//     public RelaySelectionService(RelayNodeRepository relayRepository) {
//         this.relayRepository = relayRepository;
//     }

//     // =========================================================
//     // 🔥 SELECT BEST RELAY BASED ON TRUST SCORE
//     // =========================================================
//     public RelayNode selectBestRelay() {

//         List<RelayNode> relays = relayRepository.findAll();

//         return relays.stream()
//                 .filter(r -> "ACTIVE".equals(r.getStatus()))
//                 .max(Comparator.comparingDouble(this::calculateScore))
//                 .orElse(null);
//     }

//     // =========================================================
//     // 🔥 TRUST SCORE CALCULATION (FIXED VERSION)
//     // =========================================================
//     private double calculateScore(RelayNode relay) {

//         if (relay.getLatency() == null || relay.getLatency() == 0) {
//             return 0;
//         }

//         double pdr = relay.getPacketDeliveryRatio() != null
//                 ? relay.getPacketDeliveryRatio()
//                 : 0;

//         double latency = relay.getLatency();
//         double failureRate = relay.getFailureRate() != null
//                 ? relay.getFailureRate()
//                 : 0;

//         // ✅ FIXED TRUST FORMULA (NORMALIZED)
//         double trust =
//                 (0.5 * pdr) +
//                 (0.3 * (1.0 / (1 + latency))) +
//                 (0.2 * (1 - failureRate));

//         return trust;
//     }
// }
package com.example.demo.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class RelaySelectionService {

    private final RelayNodeRepository relayRepository;

    public RelaySelectionService(RelayNodeRepository relayRepository) {
        this.relayRepository = relayRepository;
    }

    // =========================================================
    // 🔥 SELECT BEST RELAY BASED ON TRUST SCORE
    // =========================================================
    public RelayNode selectBestRelay() {

        List<RelayNode> relays = relayRepository.findAll();

        if (relays == null || relays.isEmpty()) {
            System.out.println("No relay nodes available");
            return null;
        }

        return relays.stream()
                .filter(r -> r != null && "ACTIVE".equalsIgnoreCase(r.getStatus()))
                .max(Comparator.comparingDouble(this::calculateScore))
                .orElse(null);
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