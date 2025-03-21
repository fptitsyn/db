package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeesRepository extends JpaRepository<Employees, Long>, JpaSpecificationExecutor<Employees> {}
//public interface EmployeesRepository extends JpaRepository<Employees, Long> {}