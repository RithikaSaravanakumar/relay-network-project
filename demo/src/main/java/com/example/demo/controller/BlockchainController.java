
package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.example.demo.service.BlockchainService;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/connect")
    public ResponseEntity<String> connect() {

        blockchainService.connectToBlockchain();

        boolean connected = true;

        if (connected) {
            return ResponseEntity.ok("Connected to blockchain");
        } else {
            return ResponseEntity.status(500).body("Blockchain connection failed");
        }
    }
}