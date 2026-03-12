package com.example.demo.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class RelaySelectionService {

    private final RelayNodeRepository relayNodeRepository;
    private final BlockchainService blockchainService;

    public RelaySelectionService(RelayNodeRepository relayNodeRepository, BlockchainService blockchainService) {
        this.relayNodeRepository = relayNodeRepository;
        this.blockchainService = blockchainService;
    }

    public Optional<RelayNode> selectTrustedRelay() {
        List<RelayNode> relays = relayNodeRepository.findAll();
        // filter out malicious
        List<RelayNode> candidates = relays.stream()
                .filter(r -> r.getStatus() == null || !"MALICIOUS".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());

        // fetch reputation for each and pick highest
        RelayNode best = null;
        double bestScore = -1.0;
        for (RelayNode r : candidates) {
            try {
                double score = blockchainService.getReputationFromBlockchain(r.getNodeId());
                if (score > bestScore) {
                    bestScore = score;
                    best = r;
                }
            } catch (Exception ex) {
                // skip failed reads
            }
        }
        return Optional.ofNullable(best);
    }

}
package com.example.demo.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.Node;
import com.example.demo.repository.NodeRepository;

@Service
public class RelaySelectionService {

    private final NodeRepository nodeRepository;
    private static final Logger log = LoggerFactory.getLogger(RelaySelectionService.class);

    public RelaySelectionService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    /**
     * Select the best relay node by filtering out malicious nodes and
     * returning the node with the highest trustScore.
     */
    public Optional<Node> selectBestRelayNode() {
        List<Node> candidates = nodeRepository.findAll().stream()
                .filter(n -> n.getMalicious() != null && !n.getMalicious())
                .collect(Collectors.toList());

        return candidates.stream()
                .filter(n -> n.getTrustScore() != null)
                .max(Comparator.comparingInt(Node::getTrustScore));
    }

}
