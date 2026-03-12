package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {

    @Id
    private String id;
    private String messageId;
    private String sourceNode;
    private String destinationNode;
    private String relayNode;
    private String content;
    private Long timestamp;
    private Boolean delivered;
    private Long latencyMs;

}
