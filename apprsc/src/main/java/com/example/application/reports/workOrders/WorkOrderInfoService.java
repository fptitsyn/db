package com.example.application.reports.workOrders;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderInfoService {
    private final WorkOrderInfoRepository repository;

    public WorkOrderInfoService(WorkOrderInfoRepository repository) {
        this.repository = repository;
    }

    public List<WorkOrderInfoDTO> getAllWorkOrderInfo() {
        return repository.findAllWorkOrderInfo();
    }
}