package com.example.application.data.services;

import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.employees.Employees;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long service_id;

    private String serviceName;
    private BigDecimal cost;
    private Integer timeToCompleteHours; // Храним время в часах

    @ManyToOne
    @JoinColumn(name = "type_of_device_id", nullable = false)
    private TypeOfDevice typeOfDevice;

    // Геттеры и сеттеры
    public Long getServiceId() { return service_id; }
    public void setServiceId(Long service_id) { this.service_id = service_id; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public Integer getTimeToCompleteHours() { return timeToCompleteHours; }
    public void setTimeToCompleteHours(Integer timeToCompleteHours) { this.timeToCompleteHours = timeToCompleteHours; }
    public TypeOfDevice getTypeOfDevice() { return typeOfDevice; }
    public void setTypeOfDevice(TypeOfDevice typeOfDevice) { this.typeOfDevice = typeOfDevice; }

    @Override
    public String toString() {
        return "Service{" + "id=" + service_id + ", name='" + serviceName + '\'' + '}';
    }
}
