package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeesMovingRepository extends JpaRepository<EmployeesMoving, Long> {

    List<EmployeesMoving> findByEmployee(Employees employee);

    List<EmployeesMoving> findByStaffingTable(StaffingTable staffingTable);
}