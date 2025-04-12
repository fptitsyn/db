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

    public Long getId() {
        return invoice_for_payment_id;
    }

    public void setId(Long invoice_for_payment_id) {
        this.invoice_for_payment_id = invoice_for_payment_id;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getAccruedBonuses() {
        return accruedBonuses;
    }

    public void setAccruedBonuses(BigDecimal accruedBonuses) {
        this.accruedBonuses = accruedBonuses;
    }

    public BigDecimal getDeductedBonuses() {
        return deductedBonuses;
    }

    public void setDeductedBonuses(BigDecimal deductedBonuses) {
        this.deductedBonuses = deductedBonuses;
    }

    public BigDecimal getDiscountedCost() {
        return discountedCost;
    }

    public void setDiscountedCost(BigDecimal discountedCost) {
        this.discountedCost = discountedCost;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
