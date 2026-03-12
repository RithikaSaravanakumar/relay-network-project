package com.example.demo.service;

import com.example.demo.model.Node;
import com.example.demo.repository.NodeRepository;
import com.example.demo.util.CryptoUtil;
import com.example.demo.util.HashUtil;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;

/**
 * Service layer for Node operations
 * Handles node registration, identity generation, and persistence
 */
@Service
public class NodeService {

    private NodeRepository nodeRepository;
    private CryptoUtil cryptoUtil;
    private HashUtil hashUtil;

    private static final Logger log = LoggerFactory.getLogger(NodeService.class);

    private static final Integer DEFAULT_TRUST_SCORE = 100;
    private static final Boolean DEFAULT_MALICIOUS = false;

    public NodeService(NodeRepository nodeRepository, CryptoUtil cryptoUtil, HashUtil hashUtil) {
        this.nodeRepository = nodeRepository;
        this.cryptoUtil = cryptoUtil;
        this.hashUtil = hashUtil;
    }

    /**
     * Register a new node in the network
     * Automatically generates:
     * - RSA key pair (public and private keys)
     * - Node ID (SHA-256 hash of public key)
     * - Sets default trust score to 100
     * - Sets malicious flag to false
     *
     * @return Node object with generated identity and saved to database
     * @throws Exception if key generation or hashing fails
     */
    public Node registerNode() throws Exception {
        log.info("Starting new node registration process...");

        try {
            // Step 1: Generate RSA Key Pair
            KeyPair keyPair = cryptoUtil.generateKeyPair();
            log.debug("RSA Key pair generated successfully");

            // Step 2: Encode Public Key to Base64
            String encodedPublicKey = cryptoUtil.encodePublicKey(keyPair.getPublic());
            log.debug("Public key encoded to Base64");

            // Step 3: Generate Node ID (SHA-256 hash of public key)
            String nodeId = hashUtil.generateSHA256Hash(encodedPublicKey);
            log.debug("Node ID generated: {}", nodeId);

            // Step 4: Check if node already exists
            if (nodeRepository.existsByNodeId(nodeId)) {
                log.warn("Node with ID {} already exists", nodeId);
                throw new IllegalArgumentException("Node with this identity already exists");
            }

            // Step 5: Create Node entity
            Node node = Node.builder()
                    .nodeId(nodeId)
                    .publicKey(encodedPublicKey)
                    .trustScore(DEFAULT_TRUST_SCORE)
                    .malicious(DEFAULT_MALICIOUS)
                    .createdAt(System.currentTimeMillis())
                    .build();

            // Step 6: Save to database
            Node savedNode = nodeRepository.save(node);
            log.info("Node registered successfully with ID: {}", nodeId);

            return savedNode;

        } catch (Exception e) {
            log.error("Error during node registration: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get node by nodeId
     *
     * @param nodeId the unique node identifier
     * @return Node object if found
     * @throws Exception if node not found
     */
    public Node getNodeById(String nodeId) throws Exception {
        return nodeRepository.findByNodeId(nodeId)
                .orElseThrow(() -> new Exception("Node not found with ID: " + nodeId));
    }

    /**
     * Check if a node exists
     *
     * @param nodeId the unique node identifier
     * @return true if node exists, false otherwise
     */
    public boolean nodeExists(String nodeId) {
        return nodeRepository.existsByNodeId(nodeId);
    }

}
