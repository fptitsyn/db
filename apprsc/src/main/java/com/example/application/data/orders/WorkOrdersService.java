package com.example.application.data.orders;

import org.springframework.stereotype.Service;

@Service
public class WorkOrdersService {
    private final WorkOrderRepository repository;

    public WorkOrdersService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    public WorkOrders save(WorkOrders workOrders) {
        return repository.save(workOrders);
    }

    public void delete(WorkOrders workOrders) {
        repository.delete(workOrders);
    }
}
