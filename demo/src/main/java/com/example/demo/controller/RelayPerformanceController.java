package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.model.RelayNode;
import com.example.demo.service.RelayPerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relay")
public class RelayPerformanceController {

    private final RelayPerformanceService relayPerformanceService;

    public RelayPerformanceController(RelayPerformanceService relayPerformanceService) {
        this.relayPerformanceService = relayPerformanceService;
    }

    @PostMapping("/performance")
    public ResponseEntity<?> updatePerformance(@RequestBody Map<String, Object> body) {
        String relayId = (String) body.get("relayId");
        Double latency = body.get("latency") == null ? null : ((Number) body.get("latency")).doubleValue();
        Double pdr = body.get("packetDeliveryRatio") == null ? null : ((Number) body.get("packetDeliveryRatio")).doubleValue();
        Double failureRate = body.get("failureRate") == null ? null : ((Number) body.get("failureRate")).doubleValue();

        RelayNode updated = relayPerformanceService.updatePerformanceMetrics(relayId, latency, pdr, failureRate);
        Map<String, Object> resp = new HashMap<>();
        if (updated == null) {
            resp.put("success", false);
            resp.put("message", "Relay not found: " + relayId);
            return ResponseEntity.badRequest().body(resp);
        }
        resp.put("success", true);
        resp.put("data", updated);
        return ResponseEntity.ok(resp);
    }

}
