package com.example.application.data.orders;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bonus_account_operation")
public class BonusAccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bonus_account_operation_id;

    @ManyToOne
    @JoinColumn(name = "bonus_account_id")
    private BonusAccount bonusAccount;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "operation_date")
    private LocalDate operationDate;
    @Column(name = "operation_summ")
    private BigDecimal operationSumm;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    public Long getId() {return bonus_account_operation_id;    }
    public void setId(Long bonus_account_operation_id) { this.bonus_account_operation_id = bonus_account_operation_id;    }

    public String getOperationType() {
        return operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public LocalDate getOperationDate() {
        return operationDate;
    }
    public void setOperationDate(LocalDate operationDate) {
        this.operationDate = operationDate;
    }

    public BigDecimal getOperationSumm() {
        return operationSumm;
    }
    public void setOperationSumm(BigDecimal operationSumm) {
        this.operationSumm = operationSumm;
    }

    public Orders getOrders() {
        return orders;
    }
    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    // Добавляем метод для получения названия статуса
    public String getOrderNumDate() {
        return orders != null ? "по заказу №"+Long.toString(orders.getNumberOfOrder()) + " от " + orders.getDateOfOrder() + " на  сумму: "+orders.getTotalCost() : "";
    }

    public BonusAccount getBonusAccount() {
        return bonusAccount;
    }

    public void setBonusAccount(BonusAccount bonusAccount) {
        this.bonusAccount = bonusAccount;
    }
}
