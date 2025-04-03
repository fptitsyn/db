package com.example.application.data.orders;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderComponentsService {
    private final OrderComponentsRepository repository;

    public OrderComponentsService(OrderComponentsRepository repository) {
        this.repository = repository;
    }

    public List<OrderComponents> findByOrderId(Long orderId) {
        return repository.findByOrders_Id(orderId);
    }

    public void save(OrderComponents oc) {
        repository.save(oc);
    }

    public void delete(OrderComponents oc) {
        repository.delete(oc);
    }
}
