package com.example.application.reports.employees;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeInfoService {
    private final EmployeeInfoRepository repository;

    public EmployeeInfoService(EmployeeInfoRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeInfoDTO> getAllEmployeeInfo() {
        return repository.findAllEmployeeInfo();
    }
}