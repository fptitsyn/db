package com.example.application.services;

import com.example.application.data.orders.Clients;
import com.example.application.data.orders.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsService {
    private final ClientsRepository repository;

    @Autowired
    public ClientsService(ClientsRepository repository) {
        this.repository = repository;
    }

    public Clients save(Clients client) {
        return repository.save(client);
    }

    public void delete(Clients client) {
        repository.delete(client);
    }

    public List<Clients> findAll() {
        return repository.findAll();
    }

    public Optional<Clients> findById(Long id) {
        return repository.findById(id);
    }
    public Clients findByIdWithOrders(Long id) {
        return repository.findByIdWithOrders(id).orElseThrow();
    }
}
