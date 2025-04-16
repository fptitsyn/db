package com.example.application.reports.orders;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderInfoRepository {
    private final EntityManager entityManager;

    public OrderInfoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<OrderInfoDTO> findAllOrderInfo() {
        return entityManager.createNativeQuery(
                "SELECT * FROM get_order_info()",
                "OrderInfoMapping" // Имя SQL mapping
        ).getResultList();
    }
}
