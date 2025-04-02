package com.example.application.data.employees;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeesMovingRepository extends JpaRepository<EmployeesMoving, Long> {

    List<EmployeesMoving> findByEmployee(Employees employee);

    List<EmployeesMoving> findByStaffingTable(StaffingTable staffingTable);

    Optional<EmployeesMoving> findFirstByEmployeeAndDateOfDismissalIsNullOrderByDateOfEmploymentDesc(Employees employee);
}