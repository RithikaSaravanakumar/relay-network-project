// package com.example.demo.controller;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import com.example.demo.model.Node;
// import com.example.demo.model.RelayNode;
// import com.example.demo.repository.NodeRepository;
// import com.example.demo.repository.RelayNodeRepository;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// /**
//  * NetworkController consolidated: provides node/relay listings and network status.
//  */
// @RestController
// @RequestMapping("/api/network")
// public class NetworkController {

//     private final NodeRepository nodeRepository;
//     private final RelayNodeRepository relayNodeRepository;

//     public NetworkController(NodeRepository nodeRepository, RelayNodeRepository relayNodeRepository) {
//         this.nodeRepository = nodeRepository;
//         this.relayNodeRepository = relayNodeRepository;
//     }

//     @GetMapping("/nodes")
//     public ResponseEntity<?> listNodes() {
//         List<Node> nodes = nodeRepository.findAll();
//         Map<String, Object> resp = new HashMap<>();
//         resp.put("success", true);
//         resp.put("data", nodes);
//         return ResponseEntity.ok(resp);
//     }

//     @GetMapping("/relays")
//     public ResponseEntity<?> listRelays() {
//         List<RelayNode> relays = relayNodeRepository.findAll();
//         Map<String, Object> resp = new HashMap<>();
//         resp.put("success", true);
//         resp.put("data", relays);
//         return ResponseEntity.ok(resp);
//     }

//     @GetMapping("/status")
//     public ResponseEntity<?> networkStatus() {
//         List<Node> nodes = nodeRepository.findAll();
//         int total = nodes.size();
//         long malicious = nodes.stream().filter(n -> Boolean.TRUE.equals(n.getMalicious())).count();
//         double avgTrust = nodes.stream().filter(n -> n.getTrustScore() != null).mapToInt(Node::getTrustScore).average().orElse(0.0);

//         Map<String, Object> data = new HashMap<>();
//         data.put("totalNodes", total);
//         data.put("maliciousNodes", malicious);
//         data.put("averageTrustScore", avgTrust);

//         Map<String, Object> resp = new HashMap<>();
//         resp.put("success", true);
//         resp.put("data", data);
//         return ResponseEntity.ok(resp);
//     }

// }
package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.model.Node;
import com.example.demo.model.RelayNode;
import com.example.demo.repository.NodeRepository;
import com.example.demo.repository.RelayNodeRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/network")
public class NetworkController {

    private final NodeRepository nodeRepository;
    private final RelayNodeRepository relayNodeRepository;

    public NetworkController(NodeRepository nodeRepository, RelayNodeRepository relayNodeRepository) {
        this.nodeRepository = nodeRepository;
        this.relayNodeRepository = relayNodeRepository;
    }

    @GetMapping("/nodes")
    public ResponseEntity<?> listNodes() {

        List<Node> nodes = nodeRepository.findAll();

        Map<String,Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", nodes);

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/relays")
    public ResponseEntity<?> listRelays() {

        List<RelayNode> relays = relayNodeRepository.findAll();

        Map<String,Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", relays);

        return ResponseEntity.ok(resp);
    }

    @GetMapping("/status")
    public ResponseEntity<?> networkStatus() {

        List<Node> nodes = nodeRepository.findAll();

        int total = nodes.size();

        long malicious = nodes.stream()
                .filter(Node::getMalicious)
                .count();

        double avgTrust = nodes.stream()
                .mapToInt(Node::getTrustScore)
                .average()
                .orElse(0.0);

        Map<String,Object> data = new HashMap<>();
        data.put("totalNodes", total);
        data.put("maliciousNodes", malicious);
        data.put("averageTrustScore", avgTrust);

        Map<String,Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("data", data);

        return ResponseEntity.ok(resp);
    }

}