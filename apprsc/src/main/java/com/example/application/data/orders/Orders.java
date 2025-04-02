package com.example.application.data.orders;

import com.example.application.data.employees.Employees;
import com.example.application.data.employees.Locations;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients client;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations location;

    private String product;
    private int quantity;

    // Пустой конструктор (обязателен для JPA)
    public Orders() { }

    // Конструктор с основными параметрами (опционально)
    public Orders(Clients client, Employees employee, Locations location, String product, int quantity) {
        this.client = client;
        this.employee = employee;
        this.location = location;
        this.product = product;
        this.quantity = quantity;
    }

    // Геттеры
    public Long getId() {
        return id;
    }
    public Clients getClient() {
        return client;
    }
    public Employees getEmployee() {
        return employee;
    }
    public Locations getLocation() {
        return location;
    }
    public String getProduct() {
        return product;
    }
    public int getQuantity() {
        return quantity;
    }

    // Сеттеры
    public void setClient(Clients client) {
        this.client = client;
    }
    public void setEmployee(Employees employee) { this.employee = employee; }
    public void setLocation(Locations location) { this.location = location; }
    public void setProduct(String product) {
        this.product = product;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
