
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findBySender(String sender);

    List<Message> findByReceiver(String receiver);

    List<Message> findByRelayNode(String relayNode);

    List<Message> findAllByUserId(String userId);

}