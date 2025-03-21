package com.example.application.data;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees_moving")
public class EmployeesMoving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employees_moving_id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @ManyToOne
    @JoinColumn(name = "staffing_table_id")
    private StaffingTable staffingTable;

    private LocalDate dateOfEmployment;
    private LocalDate dateOfDismissal;

    // Геттеры и сеттеры
    public Long getId() {
        return employees_moving_id;
    }
    public void setId(Long id) {
        this.employees_moving_id = id;
    }

    public Employees getEmployee() { return employee; }
    public void setEmployee(Employees employee) { this.employee = employee; }

    public StaffingTable getStaffingTable() { return staffingTable; }
    public void setStaffingTable(StaffingTable staffingTable) { this.staffingTable = staffingTable; }

    public LocalDate getDateOfEmployment() { return dateOfEmployment; }
    public void setDateOfEmployment(LocalDate dateOfEmployment) { this.dateOfEmployment = dateOfEmployment; }

    public LocalDate getDateOfDismissal() { return dateOfDismissal; }
    public void setDateOfDismissal(LocalDate dateOfDismissal) { this.dateOfDismissal = dateOfDismissal; }
}
