package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface EmployeesRepository extends JpaRepository<Employees, Long>, JpaSpecificationExecutor<Employees> {

    List<Employees> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCase(
            String lastNamePart,
            String firstNamePart,
            String middleNamePart
    );
}