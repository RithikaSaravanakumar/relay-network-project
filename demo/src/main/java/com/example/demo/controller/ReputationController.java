
// package com.example.demo.controller;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.demo.service.ReputationService;

// @RestController
// @RequestMapping("/api/reputation")
// public class ReputationController {

//     private final ReputationService reputationService;

//     public ReputationController(ReputationService reputationService) {
//         this.reputationService = reputationService;
//     }

//     /**
//      * Calculate reputation
//      */
//     @GetMapping("/calculate")
//     public ResponseEntity<?> calculate(@RequestParam String nodeId) {

//         double score = reputationService.calculateReputationScore(nodeId);

//         Map<String,Object> resp = new HashMap<>();
//         resp.put("success", true);
//         resp.put("reputationScore", score);

//         return ResponseEntity.ok(resp);
//     }

//     /**
//      * Get reputation
//      */
//     @GetMapping("/get")
//     public ResponseEntity<?> get(@RequestParam String nodeId) {

//         double score = reputationService.getReputation(nodeId);

//         Map<String,Object> resp = new HashMap<>();
//         resp.put("success", true);
//         resp.put("reputationScore", score);

//         return ResponseEntity.ok(resp);
//     }

//     /**
//      * Reward node
//      */
//     @PostMapping("/reward")
//     public ResponseEntity<?> reward(@RequestParam String nodeId) {

//         reputationService.rewardNode(nodeId);

//         Map<String,Object> resp = new HashMap<>();
//         resp.put("success", true);
//         resp.put("message", "Node rewarded");

//         return ResponseEntity.ok(resp);
//     }

//     /**
//      * Penalize node
//      */
//     @PostMapping("/penalize")
//     public ResponseEntity<?> penalize(@RequestParam String nodeId) {

//         reputationService.penalizeNode(nodeId);

//         Map<String,Object> resp = new HashMap<>();
//         resp.put("success", true);
//         resp.put("message", "Node penalized");

//         return ResponseEntity.ok(resp);
//     }
// }
package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.service.BlockchainService;

@RestController
@RequestMapping("/api/reputation")
@CrossOrigin(origins = "*")
public class ReputationController {

    private final BlockchainService blockchainService;

    public ReputationController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/{nodeId}")
    public double getReputation(@PathVariable String nodeId) {
        return blockchainService.getReputation(nodeId);
    }
}