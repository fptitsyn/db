package com.example.application.data.login;

import com.example.application.data.employees.Employees;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String username;
    private String name;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>(); // Инициализация пустым набором
    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "employee_id", unique = true)
    private Employees employee;

    public Users() {
        this.roles = new HashSet<>(); // Конструктор для инициализации
    }
    public Long getId() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    public Employees getEmployee() {return employee;}
    public void setEmployee(Employees employee) {this.employee = employee;}

}
