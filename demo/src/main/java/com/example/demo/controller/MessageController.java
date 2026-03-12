package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Message;
import com.example.demo.service.MessageService;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message sent = messageService.sendMessage(message);
        return ResponseEntity.ok(sent);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Message>> history(@RequestParam(required = false) String nodeId) {
        return ResponseEntity.ok(messageService.historyForNode(nodeId));
    }

}
package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Message;
import com.example.demo.service.MessageService;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    public static class SendMessageRequest {
        public String sourceNode;
        public String destinationNode;
        public String content;

        public String getSourceNode() { return sourceNode; }
        public String getDestinationNode() { return destinationNode; }
        public String getContent() { return content; }
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest req) {
        try {
            Message m = messageService.sendMessage(req.getSourceNode(), req.getDestinationNode(), req.getContent());
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("data", m);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            log.warn("Failed to send message: {}", e.getMessage());
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

}
