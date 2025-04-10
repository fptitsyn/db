package com.example.application.data.orders;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_for_payment")
public class InvoiceForPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoice_for_payment_id;

    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "accrued_bonuses", precision = 10, scale = 2) //Начисленные бонусы
    private BigDecimal accruedBonuses;

    @Column(name = "deducted_bonuses", precision = 10, scale = 2) //Списанные бонусы
    private BigDecimal deductedBonuses;

    @Column(name = "discounted_cost", precision = 10, scale = 2)
    private BigDecimal discountedCost;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;
}
