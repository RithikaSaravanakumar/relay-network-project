package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
        RelayNode saved = relayNodeService.registerRelay(relay);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RelayNode>> listRelays() {
        return ResponseEntity.ok(relayNodeService.listRelays());
    }

}
