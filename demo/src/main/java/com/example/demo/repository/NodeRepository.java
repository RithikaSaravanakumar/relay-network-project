package com.example.demo.repository;

import com.example.demo.model.Node;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeRepository extends MongoRepository<Node, String> {

    /**
     * Find a node by its unique nodeId (SHA-256 hash)
     */
    Optional<Node> findByNodeId(String nodeId);

    /**
     * Check if a node exists by nodeId
     */
    boolean existsByNodeId(String nodeId);

}
