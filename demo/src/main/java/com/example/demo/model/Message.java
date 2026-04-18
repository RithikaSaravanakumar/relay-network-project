
package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
public class Message {

    @Id
    private String messageId;

    private String sender;
    private String receiver;
    private String content;

    private String relayNode;

    // 🔥 MULTI-HOP FIELDS
    private List<String> hopPath;       // e.g., ["relay-abc", "relay-xyz"]
    private List<Long> hopLatencies;    // latency per hop
    private int hopCount;

    // 🔥 BLOCKCHAIN TX FIELDS
    private String txHash;
    private double trustScore;

    private long latencyMs;
    private boolean delivered;
    private long timestamp;

    // 🔥 SIMULATION FIELDS
    private double congestionLevel;
    private double packetLossRate;
    private String userId;

    public Message(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getRelayNode() { return relayNode; }
    public void setRelayNode(String relayNode) { this.relayNode = relayNode; }

    public List<String> getHopPath() { return hopPath; }
    public void setHopPath(List<String> hopPath) { this.hopPath = hopPath; }

    public List<Long> getHopLatencies() { return hopLatencies; }
    public void setHopLatencies(List<Long> hopLatencies) { this.hopLatencies = hopLatencies; }

    public int getHopCount() { return hopCount; }
    public void setHopCount(int hopCount) { this.hopCount = hopCount; }

    public String getTxHash() { return txHash; }
    public void setTxHash(String txHash) { this.txHash = txHash; }

    public double getTrustScore() { return trustScore; }
    public void setTrustScore(double trustScore) { this.trustScore = trustScore; }

    public long getLatencyMs() { return latencyMs; }
    public void setLatencyMs(long latencyMs) { this.latencyMs = latencyMs; }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getCongestionLevel() { return congestionLevel; }
    public void setCongestionLevel(double congestionLevel) { this.congestionLevel = congestionLevel; }

    public double getPacketLossRate() { return packetLossRate; }
    public void setPacketLossRate(double packetLossRate) { this.packetLossRate = packetLossRate; }
}