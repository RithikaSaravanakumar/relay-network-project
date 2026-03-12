package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.RelayNode;

@Repository
public interface RelayNodeRepository extends MongoRepository<RelayNode, String> {

    RelayNode findByRelayId(String relayId);

    List<RelayNode> findByNodeId(String nodeId);

}
