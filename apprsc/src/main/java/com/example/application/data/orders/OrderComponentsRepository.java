package com.example.application.data.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderComponentsRepository extends JpaRepository<OrderComponents, Long> {
    List<OrderComponents> findByOrders_Id(Long orderId);
}

