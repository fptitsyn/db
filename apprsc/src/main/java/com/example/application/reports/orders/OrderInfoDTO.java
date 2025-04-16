package com.example.application.reports.orders;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderInfoDTO(
        String locationName,
        String fullName,
        Long orderNumber,
        LocalDate orderDate,
        BigDecimal totalCost,
        BigDecimal discountedCost,
        BigDecimal deductedBonuses,
        BigDecimal accruedBonuses,
        String orderStatus

)
{
    public OrderInfoDTO(String locationName, String fullName, Long orderNumber, LocalDate orderDate, BigDecimal totalCost, BigDecimal discountedCost, BigDecimal deductedBonuses, BigDecimal accruedBonuses, String orderStatus) {
        this.locationName = locationName;
        this.fullName = fullName;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.discountedCost = discountedCost;
        this.deductedBonuses = deductedBonuses;
        this.accruedBonuses = accruedBonuses;
        this.orderStatus = orderStatus;
    }
}