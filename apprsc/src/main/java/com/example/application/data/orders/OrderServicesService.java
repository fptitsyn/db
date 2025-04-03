package com.example.application.data.orders;

import com.example.application.data.Services;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServicesService {
    private final OrderServicesRepository repository;

    public OrderServicesService(OrderServicesRepository repository) {
        this.repository = repository;
    }

    public List<OrderServices> findByOrderId(Long orderId) {
        return repository.findByOrders_Id(orderId);
    }

    public void save(OrderServices os) {
        repository.save(os);
    }

    public void delete(OrderServices os) {
        repository.delete(os);
    }

    public boolean serviceExistsForOrder(Orders order, Services service) {
        return repository.existsByOrdersAndServices(order, service);
    }
}
