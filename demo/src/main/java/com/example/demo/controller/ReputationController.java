package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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

    // ✅ This is what the frontend calls: /api/reputation/calculate?nodeId=relay-xxx
    @GetMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestParam String nodeId) {
        System.out.println("Calculate reputation for: " + nodeId);

        double score = blockchainService.getReputation(nodeId);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("reputationScore", score);

        return ResponseEntity.ok(score);
    }

    // ✅ Alternative endpoint: /api/reputation/{nodeId}
    @GetMapping("/{nodeId}")
    public double getReputation(@PathVariable String nodeId) {
        return blockchainService.getReputation(nodeId);
    }
}