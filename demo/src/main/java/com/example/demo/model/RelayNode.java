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
@Document(collection = "relay_nodes")
public class RelayNode {

    @Id
    private String id;
    private String relayId;
    private String nodeId;
    private Double latency;
    private Double packetDeliveryRatio;
    private Double failureRate;
    private String status; // e.g., ACTIVE, MALICIOUS, INACTIVE
    private Long createdAt;

}
