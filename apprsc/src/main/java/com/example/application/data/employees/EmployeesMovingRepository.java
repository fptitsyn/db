package com.example.application.data.employees;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeesMovingRepository extends JpaRepository<EmployeesMoving, Long> {

    Optional<EmployeesMoving> findByStaffingTableAndDateOfDismissalIsNull(StaffingTable staffingTable);
    @Query("SELECT em FROM EmployeesMoving em " +
            "WHERE em.staffingTable = :staffingTable " +
            "AND em.dateOfEmployment < :date " +
            "ORDER BY em.dateOfEmployment DESC")

    Optional<EmployeesMoving> findPreviousEmployee(
            @Param("staffingTable") StaffingTable staffingTable,
            @Param("date") LocalDate date
    );

    List<EmployeesMoving> findByEmployee(Employees employee);

    List<EmployeesMoving> findByStaffingTable(StaffingTable staffingTable);

    Optional<EmployeesMoving> findFirstByEmployeeAndDateOfDismissalIsNullOrderByDateOfEmploymentDesc(Employees employee);
}