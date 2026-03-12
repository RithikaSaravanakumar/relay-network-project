package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    Optional<Message> findByMessageId(String messageId);

    List<Message> findBySourceNode(String sourceNode);

    List<Message> findByRelayNode(String relayNode);

}
