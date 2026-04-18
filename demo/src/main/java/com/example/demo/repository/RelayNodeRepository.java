
package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.RelayNode;

public interface RelayNodeRepository extends MongoRepository<RelayNode, String> {

    Optional<RelayNode> findByRelayId(String relayId);

    List<RelayNode> findAllByUserId(String userId);

}