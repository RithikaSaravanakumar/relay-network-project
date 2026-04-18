
// package com.example.demo.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import com.example.demo.model.Message;
// import com.example.demo.service.MessageService;

// @RestController
// @RequestMapping("/api/messages")
// @CrossOrigin(origins = "*")
// public class MessageController {

//     private final MessageService messageService;

//     public MessageController(MessageService messageService) {
//         this.messageService = messageService;
//     }

//     @PostMapping("/send")
//     public ResponseEntity<?> sendMessage(
//             @RequestParam String sourceNode,
//             @RequestParam String destinationNode,
//             @RequestParam String content) {

//         Message message = messageService.sendMessage(sourceNode, destinationNode, content);
//         return ResponseEntity.ok(message);
//     }
// }
package com.example.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Message;
import com.example.demo.service.MessageService;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> payload) {
        String sourceNode = payload.get("sourceNode");
        String destinationNode = payload.get("destinationNode");
        String content = payload.get("content");
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Message message = messageService.sendMessage(sourceNode, destinationNode, content, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", message);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Message>> listMessages() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(messageService.listMessages(userId));
    }
}