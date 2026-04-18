package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final BlockchainService blockchainService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, BlockchainService blockchainService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.blockchainService = blockchainService;
    }

    private String generateSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().substring(0, 16);
        } catch (Exception e) {
            return UUID.randomUUID().toString().substring(0, 16);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Module 2: Node Identity Generation
        String rawBase = UUID.randomUUID().toString() + userRequest.getUsername();
        String nodeId = "node-" + generateSha256(rawBase);
        String publicKey = "PUBKEY-" + generateSha256(rawBase + "key");

        user.setNodeId(nodeId);
        user.setPublicKey(publicKey);
        
        userRepository.save(user);

        // Store identity on Blockchain (Module 7)
        new Thread(() -> {
            try {
                blockchainService.registerNodeOnBlockchain(nodeId, publicKey);
            } catch (Exception e) {
                System.out.println("Async blockchain register failed: " + e.getMessage());
            }
        }).start();

        String token = jwtUtil.generateToken(user.getUsername(), user.getNodeId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("nodeId", user.getNodeId());
        response.put("publicKey", user.getPublicKey());
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            User user = userOpt.get();
            String token = jwtUtil.generateToken(user.getUsername(), user.getNodeId());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("nodeId", user.getNodeId());
            response.put("publicKey", user.getPublicKey());
            response.put("username", user.getUsername());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate() {
        return ResponseEntity.ok(Map.of("valid", true));
    }
}
