package com.example.application.data.employees;

import com.example.application.data.locations.Locations;
import com.example.application.data.orders.WorkOrders;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedule_id;
    @Column(name = "time_interval")
    private String timeInterval;
    @Column(name = "work_day")
    private LocalDate workDay;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations location;

    @ManyToOne
    @JoinColumn(name = "work_orders_id")
    private WorkOrders workOrders;

    //Геттеры и сеттеры
    public Long getId() {
        return schedule_id;
    }

    public void setId(Long schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public LocalDate getWorkDay() {
        return workDay;
    }

    public void setWorkDay(LocalDate workDay) {
        this.workDay = workDay;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    public WorkOrders getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(WorkOrders workOrders) {
        this.workOrders = workOrders;
    }
}
