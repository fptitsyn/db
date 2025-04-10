package com.example.application.data.orders;

import com.example.application.data.components.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrdersService {
    private final WorkOrderRepository repository;

    public WorkOrdersService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    public List<WorkOrders> findAll() {
        return repository.findAll();
    }

    public WorkOrders save(WorkOrders workOrders) {
        return repository.save(workOrders);
    }

    public void delete(WorkOrders workOrders) {
        repository.delete(workOrders);
    }
}
