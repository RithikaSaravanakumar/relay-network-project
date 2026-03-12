package com.example.demo.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.RelayNode;
import com.example.demo.repository.RelayNodeRepository;

@Service
public class RelayNodeService {

    private final RelayNodeRepository relayNodeRepository;

    public RelayNodeService(RelayNodeRepository relayNodeRepository) {
        this.relayNodeRepository = relayNodeRepository;
    }

    public RelayNode registerRelay(RelayNode relay) {
        relay.setCreatedAt(Instant.now().toEpochMilli());
        return relayNodeRepository.save(relay);
    }

    public List<RelayNode> listRelays() {
        return relayNodeRepository.findAll();
    }

    public RelayNode findByRelayId(String relayId) {
        return relayNodeRepository.findByRelayId(relayId);
    }

    public RelayNode save(RelayNode relay) {
        return relayNodeRepository.save(relay);
    }

}
