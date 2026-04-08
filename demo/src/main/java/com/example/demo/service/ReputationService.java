
// package com.example.demo.service;

// import java.util.List;

// import org.springframework.stereotype.Service;

// import com.example.demo.model.Node;
// import com.example.demo.model.RelayNode;
// import com.example.demo.repository.NodeRepository;
// import com.example.demo.repository.RelayNodeRepository;

// @Service
// public class ReputationService {

//     private final NodeRepository nodeRepository;
//     private final RelayNodeRepository relayRepository;
//     private final BlockchainService blockchainService;

//     public ReputationService(NodeRepository nodeRepository,
//                              RelayNodeRepository relayRepository,
//                              BlockchainService blockchainService) {

//         this.nodeRepository = nodeRepository;
//         this.relayRepository = relayRepository;
//         this.blockchainService = blockchainService;
//     }

//     /**
//      * Calculate reputation score
//      */
//     public double calculateReputationScore(String nodeId){

//         Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

//         if(node == null){
//             throw new RuntimeException("Node not found");
//         }

//         List<RelayNode> relays = relayRepository.findAll();

//         double latency = 0;
//         double delivery = 0;
//         double failure = 0;

//         int count = relays.size();

//         if(count == 0){
//             return node.getReputationScore();
//         }

//         for(RelayNode relay : relays){

//             latency += relay.getLatency();
//             delivery += relay.getPacketDeliveryRatio();
//             failure += relay.getFailureRate();
//         }

//         double avgLatency = latency / count;
//         double avgDelivery = delivery / count;
//         double avgFailure = failure / count;

//         double reputation =
//                 (avgDelivery * 50)
//                 - (avgLatency * 0.2)
//                 - (avgFailure * 100);

//         if(reputation < 0){
//             reputation = 0;
//         }

//         node.setReputationScore(reputation);

//         nodeRepository.save(node);

//         try{
//             blockchainService.storeReputation(nodeId, reputation);
//         }catch(Exception e){
//             System.out.println("Blockchain update failed");
//         }

//         return reputation;
//     }

//     /**
//      * Get reputation
//      */
//     public double getReputation(String nodeId){

//         Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

//         if(node == null){
//             throw new RuntimeException("Node not found");
//         }

//         return node.getReputationScore();
//     }

//     /**
//      * Reward node
//      */
//     public void rewardNode(String nodeId){

//         Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

//         if(node == null){
//             throw new RuntimeException("Node not found");
//         }

//         double reputation = node.getReputationScore();

//         reputation += 5;

//         node.setReputationScore(reputation);

//         nodeRepository.save(node);
//     }

//     /**
//      * Penalize node
//      */
//     public void penalizeNode(String nodeId){

//         Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

//         if(node == null){
//             throw new RuntimeException("Node not found");
//         }

//         double reputation = node.getReputationScore();

//         reputation -= 5;

//         if(reputation < 0){
//             reputation = 0;
//         }

//         node.setReputationScore(reputation);

//         nodeRepository.save(node);
//     }

// }
package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Node;
import com.example.demo.model.RelayNode;
import com.example.demo.repository.NodeRepository;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class ReputationService {

    private final NodeRepository nodeRepository;
    private final RelayNodeRepository relayRepository;
    private final BlockchainService blockchainService;

    public ReputationService(NodeRepository nodeRepository,
                             RelayNodeRepository relayRepository,
                             BlockchainService blockchainService) {

        this.nodeRepository = nodeRepository;
        this.relayRepository = relayRepository;
        this.blockchainService = blockchainService;
    }

    /**
     * Calculate reputation score
     */
    public double calculateReputationScore(String nodeId){

        Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

        // Prevent crash if node not found
        if(node == null){
            System.out.println("Node not found for reputation calculation: " + nodeId);
            return 0;
        }

        List<RelayNode> relays = relayRepository.findAll();

        if(relays.isEmpty()){
            return node.getReputationScore();
        }

        double latency = 0;
        double delivery = 0;
        double failure = 0;

        for(RelayNode relay : relays){

            latency += relay.getLatency();
            delivery += relay.getPacketDeliveryRatio();
            failure += relay.getFailureRate();
        }

        double avgLatency = latency / relays.size();
        double avgDelivery = delivery / relays.size();
        double avgFailure = failure / relays.size();

        double reputation =
                (avgDelivery * 50)
                - (avgLatency * 0.2)
                - (avgFailure * 100);

        if(reputation < 0){
            reputation = 0;
        }

        node.setReputationScore(reputation);

        nodeRepository.save(node);

        try{
            blockchainService.storeReputation(nodeId, reputation);
        }catch(Exception e){
            System.out.println("Blockchain update failed");
        }

        return reputation;
    }

    /**
     * Get reputation
     */
    public double getReputation(String nodeId){

        Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

        if(node == null){
            return 0;
        }

        return node.getReputationScore();
    }

    /**
     * Reward node
     */
    public void rewardNode(String nodeId){

        Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

        if(node == null){
            return;
        }

        node.setReputationScore(node.getReputationScore() + 5);

        nodeRepository.save(node);
    }

    /**
     * Penalize node
     */
    public void penalizeNode(String nodeId){

        Node node = nodeRepository.findByNodeId(nodeId).orElse(null);

        if(node == null){
            return;
        }

        double reputation = node.getReputationScore() - 5;

        if(reputation < 0){
            reputation = 0;
        }

        node.setReputationScore(reputation);

        nodeRepository.save(node);
    }
}