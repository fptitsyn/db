package com.example.application.data;

import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.employees.Employees;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    private String serviceName;
    private Double cost;
    private Integer timeToCompleteMinutes; // Храним время в минутах

    @ManyToOne
    @JoinColumn(name = "type_of_device_id", nullable = false)
    private TypeOfDevice typeOfDevice;

    @ManyToMany(fetch = FetchType.EAGER) // Или LAZY с открытой сессией
    @JoinTable(
            name = "employee_service",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<Employees> employees = new HashSet<>();

    // Геттеры и сеттеры
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
    public Integer getTimeToCompleteMinutes() { return timeToCompleteMinutes; }
    public void setTimeToCompleteMinutes(Integer timeToCompleteMinutes) { this.timeToCompleteMinutes = timeToCompleteMinutes; }
    public TypeOfDevice getTypeOfDevice() { return typeOfDevice; }
    public void setTypeOfDevice(TypeOfDevice typeOfDevice) { this.typeOfDevice = typeOfDevice; }
    public Set<Employees> getEmployees() { return employees; }
    public void setEmployees(Set<Employees> employees) { this.employees = employees; }

    @Override
    public String toString() {
        return "Service{" + "id=" + serviceId + ", name='" + serviceName + '\'' + '}';
    }
}
