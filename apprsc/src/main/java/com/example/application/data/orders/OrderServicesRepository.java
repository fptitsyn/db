package com.example.application.data.orders;

import com.example.application.data.services.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderServicesRepository extends JpaRepository<OrderServices, Long> {

    // Найти все услуги для конкретного заказа
    List<OrderServices> findByOrders_Id(Long orderId);

    // Удалить все услуги для конкретного заказа
    void deleteByOrders(Orders order);

    // Проверить существование связи заказ-услуга
    boolean existsByOrdersAndServices(Orders order, Services service);

}