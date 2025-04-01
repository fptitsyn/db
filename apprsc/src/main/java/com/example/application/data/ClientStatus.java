package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "client_status")
public class ClientStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_status_id;
    private String status;

    public Long getId() {
        return client_status_id;
    }

    public void setId(Long client_status_id) {
        this.client_status_id = client_status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
