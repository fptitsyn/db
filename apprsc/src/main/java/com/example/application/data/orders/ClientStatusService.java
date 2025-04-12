package com.example.application.data.orders;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientStatusService {
    private final ClientStatusRepository repository;

    public ClientStatusService(ClientStatusRepository repository) {
        this.repository = repository;
    }

    public List<ClientStatus> findByClientId(Long clientId) {
        return repository.findByClientId(clientId);
    }
}
