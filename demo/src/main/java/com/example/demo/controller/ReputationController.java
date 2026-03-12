package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.service.BlockchainService;
import com.example.demo.service.ReputationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reputation")
public class ReputationController {

    private final ReputationService reputationService;
    private final BlockchainService blockchainService;

    public ReputationController(ReputationService reputationService, BlockchainService blockchainService) {
        this.reputationService = reputationService;
        this.blockchainService = blockchainService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestParam String nodeId) {
        double score = reputationService.calculateReputationScore(nodeId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", score);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getFromChain(@RequestParam String nodeId) {
        try {
            double score = blockchainService.getReputationFromBlockchain(nodeId);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("data", score);
            return ResponseEntity.ok(resp);
        } catch (IOException e) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("message", "Failed to read blockchain: " + e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

}
