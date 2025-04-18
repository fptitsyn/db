package com.example.application.data.employees;

import com.example.application.data.locations.Locations;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "staffing_table")
public class StaffingTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffing_table_id;


    private String position;
    private BigDecimal salary;
    private String department;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations location;

    // Геттеры
    public Long getId() {
        return staffing_table_id;
    }
    public String getPosition() {
        return position;
    }
    public BigDecimal getSalary() {
        return salary;
    }
    public String getDepartment() {
        return department;
    }
    public Locations getLocation() { return location;    }

    // Сеттеры
    public void setId(Long id) {
        this.staffing_table_id = id;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setLocation(Locations location) {
        this.location = location;
    }
    public String getName() {
        return location != null ? location.getName() : "";
    }

}
