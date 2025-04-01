package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "clients")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;

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
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private String gender;
    @Column(name = "city_of_residence")
    private String cityOfResidence;

    @OneToOne
    @JoinColumn(name = "client_status_id")
    private ClientStatus clientStatus;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @OneToOne(mappedBy = "clients") // mappedBy указывает на поле в BonusAccount
    private BonusAccount bonusAccount;

    // Геттеры, сеттеры, конструкторы
    // Пустой конструктор (обязателен для JPA)
    public Clients() {
    }

    // Конструктор с параметром имени (опционально)
    public Clients(String firstName) {
        this.firstName = firstName;
    }

    // Геттеры
    public Long getId() {
        return client_id;
    }
    public String getFirstName() { return firstName; }
    public String getLastName() {
        return lastName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone_number;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public String getComment() {
        return comment;
    }
    public String getGender() {
        return gender;
    }
    public String getCityOfResidence() {
        return cityOfResidence;
    }

    public ClientStatus getClientStatus() { return clientStatus;    }

    public String getFullName() {
        return lastName + " " + firstName + " " + (middleName != null ? middleName : "");
    }

    public List<Orders> getOrders() {return Collections.unmodifiableList(orders);}// Защита от неконтролируемых изменений
    public BonusAccount getBonusAccount() {return bonusAccount;}

    // Сеттеры
    public void setFirstName(String first_name) { this.firstName = first_name; }
    public void setLastName(String last_name) {
        this.lastName = last_name;
    }
    public void setMiddleName(String middle_name) {
        this.middleName = middle_name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone_number) {
        this.phone_number = phone_number;
    }
    public void setDateOfBirth(LocalDate date_of_birth) {this.dateOfBirth = date_of_birth; }
    public void setComment(String comment) { this.comment = comment;    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setCityOfResidence(String cityOfResidence) {
        this.cityOfResidence = cityOfResidence;
    }
    public void setClientStatus(ClientStatus clientStatus) {
        this.clientStatus = clientStatus;
    }

    // Методы для управления связью с Order
    public void addOrder(Orders order) {
        orders.add(order);
        order.setClient(this);
    }
    public void removeOrder(Orders order) {
        orders.remove(order);
        order.setClient(null);
    }
}