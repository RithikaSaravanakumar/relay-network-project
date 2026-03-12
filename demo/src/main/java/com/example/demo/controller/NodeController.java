package com.example.demo.controller;

import com.example.demo.model.Node;
import com.example.demo.service.NodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    private static final Logger log = LoggerFactory.getLogger(NodeController.class);

    @Autowired
    private NodeService nodeService;

    /**
     * Register a new node in the P2P network
     * Automatically generates node identity including:
     * - RSA public and private keys
     * - Node ID (SHA-256 hash of public key)
     * - Default trust score (100)
     * - Default malicious flag (false)
     *
     * @return ResponseEntity containing node registration details
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerNode() {
        try {
            log.info("Received node registration request");

            // Register the node (generates keys and saves to DB)
            Node registeredNode = nodeService.registerNode();

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Node registered successfully");
            response.put("data", new NodeRegistrationResponse(
                    registeredNode.getNodeId(),
                    registeredNode.getPublicKey(),
                    registeredNode.getTrustScore(),
                    registeredNode.getMalicious(),
                    registeredNode.getId()
            ));

            log.info("Node registered successfully: {}", registeredNode.getNodeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.warn("Node registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(createErrorResponse("Node already exists: " + e.getMessage()));

        } catch (Exception e) {
            log.error("Unexpected error during node registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error registering node: " + e.getMessage()));
        }
    }

    /**
     * Get node information by nodeId
     *
     * @param nodeId the unique node identifier
     * @return ResponseEntity containing node details
     */
    @GetMapping("/{nodeId}")
    public ResponseEntity<?> getNode(@PathVariable String nodeId) {
        try {
            log.info("Retrieving node with ID: {}", nodeId);

            Node node = nodeService.getNodeById(nodeId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", new NodeRegistrationResponse(
                    node.getNodeId(),
                    node.getPublicKey(),
                    node.getTrustScore(),
                    node.getMalicious(),
                    node.getId()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.warn("Node not found: {}", nodeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Node not found: " + nodeId));
        }
    }

    /**
     * Health check endpoint
     *
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "Relay Node Service");
        return ResponseEntity.ok(response);
    }

    /**
     * Helper method to create error response
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        return errorResponse;
    }

    /**
     * Inner class for node registration response
     */
    public static class NodeRegistrationResponse {
        public String nodeId;
        public String publicKey;
        public Integer trustScore;
        public Boolean malicious;
        public String databaseId;

        public NodeRegistrationResponse(String nodeId, String publicKey, Integer trustScore,
                                       Boolean malicious, String databaseId) {
            this.nodeId = nodeId;
            this.publicKey = publicKey;
            this.trustScore = trustScore;
            this.malicious = malicious;
            this.databaseId = databaseId;
        }

        // Getters for JSON serialization
        public String getNodeId() { return nodeId; }
        public String getPublicKey() { return publicKey; }
        public Integer getTrustScore() { return trustScore; }
        public Boolean getMalicious() { return malicious; }
        public String getDatabaseId() { return databaseId; }
    }

}
