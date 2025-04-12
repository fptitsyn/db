package com.example.application.data.orders;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_status")
public class ClientStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_status_id;
    private String status;
    @Column(name = "bonus_percentage", precision = 10, scale = 2)
    private BigDecimal bonusPercentage;

    @OneToMany(mappedBy = "clientStatus")
    private List<Clients> clients = new ArrayList<>();
    
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

    public BigDecimal getBonusPercentage() {return bonusPercentage;}
    public void setBonusPercentage(BigDecimal bonusPercentage) {this.bonusPercentage = bonusPercentage;}
}
