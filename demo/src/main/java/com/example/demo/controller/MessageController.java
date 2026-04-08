
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

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> sendMessage(
            @RequestParam("sourceNode") String sourceNode,
            @RequestParam("destinationNode") String destinationNode,
            @RequestParam("content") String content) {

        Message message = messageService.sendMessage(sourceNode, destinationNode, content);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", message);

        return ResponseEntity.ok(response);
    }
}