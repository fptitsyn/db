package com.example.application.data;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Duration;

@Entity
@Table(name = "services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Services() {}

    private String service_name;
    private BigDecimal cost;
    private Duration time_to_complete;

    public Services(String service_name, BigDecimal cost, Duration time_to_complete) {
        this.service_name = service_name;
        this.cost = cost;
        this.time_to_complete = time_to_complete;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Duration getTime_to_complete() {
        return time_to_complete;
    }

    public void setTime_to_complete(Duration time_to_complete) {
        this.time_to_complete = time_to_complete;
    }
}
