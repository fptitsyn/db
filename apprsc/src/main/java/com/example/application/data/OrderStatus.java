package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_status_id;
    private String status;

    public Long getId() {
        return order_status_id;
    }

    public void setId(Long order_status_id) {
        this.order_status_id = order_status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
