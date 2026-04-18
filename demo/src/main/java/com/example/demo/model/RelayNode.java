// package com.example.demo.model;

// import org.springframework.data.annotation.Id;
// import org.springframework.data.mongodb.core.mapping.Document;

// @Document(collection = "relay_nodes")
// public class RelayNode {

//     @Id
//     private String id;

//     private String relayId;
//     private String nodeId;

//     private Double latency;
//     private Double packetDeliveryRatio;
//     private Double failureRate;

//     private String status;

//     private Long createdAt;

//     public RelayNode() {}

//     public String getId() {
//         return id;
//     }

//     public String getRelayId() {
//         return relayId;
//     }

//     public void setRelayId(String relayId) {
//         this.relayId = relayId;
//     }

//     public String getNodeId() {
//         return nodeId;
//     }

//     public void setNodeId(String nodeId) {
//         this.nodeId = nodeId;
//     }

//     public Double getLatency() {
//         return latency;
//     }

//     public void setLatency(Double latency) {
//         this.latency = latency;
//     }

//     public Double getPacketDeliveryRatio() {
//         return packetDeliveryRatio;
//     }

//     public void setPacketDeliveryRatio(Double packetDeliveryRatio) {
//         this.packetDeliveryRatio = packetDeliveryRatio;
//     }

//     public Double getFailureRate() {
//         return failureRate;
//     }

//     public void setFailureRate(Double failureRate) {
//         this.failureRate = failureRate;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public void setStatus(String status) {
//         this.status = status;
//     }

//     public Long getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(Long createdAt) {
//         this.createdAt = createdAt;
//     }
// }
package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "relay_nodes")
public class RelayNode {

    @Id
    private String id;

    private String relayId;
    private String nodeId;
    private String publicKey;

    private Double latency;
    private Double packetDeliveryRatio;
    private Double failureRate;

    private String status;
    private Long createdAt;

    // 🔥 NEW FIELDS (IMPORTANT)
    private Integer totalRequests;
    private Integer successCount;
    private Integer failureCount;
    private Double trust;
    private String userId;

    // ===== GETTERS & SETTERS =====

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getRelayId() {
        return relayId;
    }

    public void setRelayId(String relayId) {
        this.relayId = relayId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Double getLatency() {
        return latency;
    }

    public void setLatency(Double latency) {
        this.latency = latency;
    }

    public Double getPacketDeliveryRatio() {
        return packetDeliveryRatio;
    }

    public void setPacketDeliveryRatio(Double packetDeliveryRatio) {
        this.packetDeliveryRatio = packetDeliveryRatio;
    }

    public Double getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(Double failureRate) {
        this.failureRate = failureRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    // 🔥 NEW GETTERS & SETTERS

    public Integer getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Integer totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public Double getTrust() {
        return trust == null ? 0.5 : trust;
    }

    public void setTrust(Double trust) {
        this.trust = trust == null ? 0.5 : Math.max(0.0, Math.min(1.0, trust));
    }
}