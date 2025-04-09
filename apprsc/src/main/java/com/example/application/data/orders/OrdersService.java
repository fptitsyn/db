package com.example.application.data.orders;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    private final OrdersRepository repository;
    private final EntityManager entityManager;

    @Autowired
    public OrdersService(OrdersRepository repository,
                         EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Orders save(Orders order) {
        return repository.saveAndFlush(order);
    }

    public void delete(Orders order) {
        repository.delete(order);
    }

    public List<Orders> findByClientId(Long clientId) {
        return repository.findByClientId(clientId);
    }

    /*@Transactional
    public void refresh(Orders order) {
        entityManager.refresh(order);
    }

     */
}