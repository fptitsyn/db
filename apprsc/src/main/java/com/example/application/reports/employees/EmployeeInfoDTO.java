package com.example.application.reports.employees;

import java.time.LocalDate;

public record EmployeeInfoDTO(
        String lastName,
        String firstName,
        String middleName,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        String comment,
        Integer age,
        String department,
        String position,
        Integer salary,
        String workplace,
        Integer experience
) {
    // Конструктор для JPA
    public EmployeeInfoDTO(
            String lastName,
            String firstName,
            String middleName,
            LocalDate dateOfBirth,
            String phoneNumber,
            String email,
            String comment,
            Integer age,
            String department,
            String position,
            Integer salary,
            String workplace,
            Integer experience
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.comment = comment;
        this.age = age;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.workplace = workplace;
        this.experience = experience;

    }
}