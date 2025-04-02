package com.example.application.data.orders;

import jakarta.persistence.*;

@Entity
@Table(name = "work_order_status")
public class WorkOrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long work_order_status_id;
    private String status;

    public Long getId() {
        return work_order_status_id;
    }

    public void setId(Long order_status_id) {
        this.work_order_status_id = order_status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
