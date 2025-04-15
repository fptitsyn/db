package com.example.application.data.employees;

public class EmployeeWithPositionDTO {
    private final Long employeeId;
    private final String fullNameWithPosition;

    public EmployeeWithPositionDTO(Long employeeId, String fullNameWithPosition) {
        this.employeeId = employeeId;
        this.fullNameWithPosition = fullNameWithPosition;
    }

    // Геттеры
    public Long getEmployeeId() { return employeeId; }
    public String getFullNameWithPosition() { return fullNameWithPosition; }
}