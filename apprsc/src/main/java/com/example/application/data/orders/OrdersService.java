package com.example.application.data.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    private final OrdersRepository repository;

    @Autowired
    public OrdersService(OrdersRepository repository) {
        this.repository = repository;
    }

    public Orders save(Orders order) {
        return repository.save(order);
    }

    public void delete(Orders order) {
        repository.delete(order);
    }

    public List<Orders> findByClientId(Long clientId) {
        return repository.findByClientId(clientId);
    }
}