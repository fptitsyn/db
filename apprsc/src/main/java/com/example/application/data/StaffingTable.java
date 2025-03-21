package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "staffing_table")
public class StaffingTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffing_table_id;


    private String position;
    private int salary;

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
    public int getSalary() {
        return salary;
    }
    public Locations getLocation() { return location;    }

    // Сеттеры
    public void setId(Long id) {
        this.staffing_table_id = id;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public void setLocation(Locations location) {
        this.location = location;
    }
    public String getName() {
        return location != null ? location.getName() : "";
    }

}
