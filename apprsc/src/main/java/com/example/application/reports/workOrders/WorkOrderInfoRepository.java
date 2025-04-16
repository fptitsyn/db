package com.example.application.reports.workOrders;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkOrderInfoRepository {
    private final EntityManager entityManager;

    public WorkOrderInfoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<WorkOrderInfoDTO> findAllWorkOrderInfo() {
        return entityManager.createNativeQuery(
                "SELECT * FROM get_workorder_info()",
                "WorkOrderInfoMapping" // Имя SQL mapping
        ).getResultList();
    }
}
