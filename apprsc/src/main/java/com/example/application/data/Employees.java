package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employee_id;

    @ManyToMany(fetch = FetchType.EAGER) // Или LAZY с открытой сессией
    @JoinTable(
            name = "employee_service",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Services> services = new HashSet<>();

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Email
    private String email;
    private String phone_number;
    private String comment;
    private LocalDate date_of_birth;
    private String gender;

    public Long getId() { return employee_id;  }
    public void setId(Long id) {
        this.employee_id = id;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String first_name) { this.firstName = first_name; }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String last_name) {
        this.lastName = last_name;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middle_name) {
        this.middleName = middle_name;
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
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + (middleName != null ? middleName : "");
    }

    public Set<Services> getServices() { return services; }
    public void setServices(Set<Services> services) { this.services = services; }
}
