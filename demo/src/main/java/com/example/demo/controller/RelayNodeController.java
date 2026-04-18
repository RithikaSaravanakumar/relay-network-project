package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.RelayNode;
import com.example.demo.service.RelayNodeService;

@RestController
@RequestMapping("/api/relay")
public class RelayNodeController {

    private final RelayNodeService relayNodeService;

    public RelayNodeController(RelayNodeService relayNodeService) {
        this.relayNodeService = relayNodeService;
    }

    @PostMapping("/register")
    public ResponseEntity<RelayNode> registerRelay(@RequestBody RelayNode relay) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        RelayNode saved = relayNodeService.registerRelay(relay, userId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RelayNode>> listRelays() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(relayNodeService.listRelays(userId));
    }

    // Module 4: Relay Node Creation
    @PostMapping("/create")
    public ResponseEntity<RelayNode> createRelay() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        RelayNode relay = new RelayNode();
        // The service automatically defaults values and hashes ID
        RelayNode saved = relayNodeService.registerRelay(relay, userId);
        return ResponseEntity.ok(saved);
    }
}
