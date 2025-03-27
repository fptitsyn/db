package com.example.application.services;

import com.example.application.data.Employees;
import com.example.application.data.EmployeesMoving;
import com.example.application.data.EmployeesMovingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeesMovingService {
    private final EmployeesMovingRepository repository;

    public EmployeesMovingService(EmployeesMovingRepository repository) {
        this.repository = repository;
    }

    public Optional<EmployeesMoving> findActiveByEmployee(Employees employee) {
        return repository.findFirstByEmployeeAndDateOfDismissalIsNullOrderByDateOfEmploymentDesc(employee);
    }
}
