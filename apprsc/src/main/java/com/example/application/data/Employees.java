package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employee_id;

    private String first_name;
    private String last_name;
    private String middle_name;
    @Email
    private String email;
    private String phone_number;
    private String comment;
    private LocalDate date_of_birth;

    public Long getId() { return employee_id;  }
    public void setId(Long id) {
        this.employee_id = id;
    }

    public String getFirstName() { return first_name; }
    public void setFirstName(String first_name) { this.first_name = first_name; }
    public String getLastName() {
        return last_name;
    }
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }
    public String getMiddleName() {
        return middle_name;
    }
    public void setMiddleName(String middle_name) {
        this.middle_name = middle_name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone_number;
    }
    public void setPhone(String phone_number) {
        this.phone_number = phone_number;
    }
    public LocalDate getDateOfBirth() {
        return date_of_birth;
    }
    public void setDateOfBirth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getFullName() {
        return last_name + " " + first_name + " " + (middle_name != null ? middle_name : "");
    }
}