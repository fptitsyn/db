package com.example.application.reports.workOrders;

import java.math.BigDecimal;
import java.time.LocalDate;

public record WorkOrderInfoDTO(
        String fullName,
        Long orderNumber,
        LocalDate orderDate,
        Integer servicesAmount,
        Integer hoursAmount,
        String orderStatus
)
{
    // Конструктор для JPA

    public WorkOrderInfoDTO(String fullName, Long orderNumber, LocalDate orderDate, Integer servicesAmount, Integer hoursAmount, String orderStatus) {
        this.fullName = fullName;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.servicesAmount = servicesAmount;
        this.hoursAmount = hoursAmount;
        this.orderStatus = orderStatus;
    }
}