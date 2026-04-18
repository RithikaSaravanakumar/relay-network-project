
package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Node;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends MongoRepository<Node, String> {

    Optional<Node> findByNodeId(String nodeId);

    boolean existsByNodeId(String nodeId);

    List<Node> findAllByUserId(String userId);

}