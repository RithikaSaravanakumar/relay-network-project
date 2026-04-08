
package com.example.demo.service;

import com.example.demo.model.Node;
import com.example.demo.repository.NodeRepository;
import com.example.demo.util.CryptoUtil;
import com.example.demo.util.HashUtil;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;
    private final CryptoUtil cryptoUtil;
    private final HashUtil hashUtil;
    private final BlockchainService blockchainService;

    private static final Logger log = LoggerFactory.getLogger(NodeService.class);

    private static final Integer DEFAULT_TRUST_SCORE = 100;
    private static final Boolean DEFAULT_MALICIOUS = false;

    @Autowired
    public NodeService(NodeRepository nodeRepository,
                       CryptoUtil cryptoUtil,
                       HashUtil hashUtil,
                       BlockchainService blockchainService) {

        this.nodeRepository = nodeRepository;
        this.cryptoUtil = cryptoUtil;
        this.hashUtil = hashUtil;
        this.blockchainService = blockchainService;
    }

    public Node registerNode() throws Exception {

        log.info("Starting new node registration process...");

        KeyPair keyPair = cryptoUtil.generateKeyPair();

        String encodedPublicKey = cryptoUtil.encodePublicKey(keyPair.getPublic());

        String nodeId = hashUtil.generateSHA256Hash(encodedPublicKey);

        if(nodeRepository.existsByNodeId(nodeId)){
            throw new RuntimeException("Node already exists");
        }

        Node node = new Node();

        node.setNodeId(nodeId);
        node.setPublicKey(encodedPublicKey);
        node.setTrustScore(DEFAULT_TRUST_SCORE);
        node.setMalicious(DEFAULT_MALICIOUS);
        node.setCreatedAt(System.currentTimeMillis());

        Node savedNode = nodeRepository.save(node);

        log.info("Node saved in MongoDB: {}", nodeId);

        try {

            blockchainService.registerNodeOnBlockchain(
                    nodeId,
                    encodedPublicKey
            );

            log.info("Node registered on blockchain");

        } catch (Exception e) {

            log.error("Blockchain registration failed", e);

        }

        return savedNode;
    }

    public Node getNodeById(String nodeId) throws Exception {

        return nodeRepository.findByNodeId(nodeId)
                .orElseThrow(() ->
                        new Exception("Node not found: " + nodeId)
                );
    }

    public boolean nodeExists(String nodeId){

        return nodeRepository.existsByNodeId(nodeId);

    }

}