package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "nodes")
public class Node {

    @Id
    private String id;
    private String nodeId;
    private String publicKey;
    private Integer trustScore;
    private Boolean malicious;
    private Long createdAt;
}
