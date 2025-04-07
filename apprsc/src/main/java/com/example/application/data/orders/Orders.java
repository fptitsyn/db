package com.example.application.data.orders;

import com.example.application.data.employees.Employees;
import com.example.application.data.locations.Locations;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orders_id;

    @Column(name = "number_of_order")
    private Long numberOfOrder;

    @Column(name = "date_of_order")
    private LocalDate dateOfOrder;

    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations location;

    @OneToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

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
        return orders_id;
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
    public Long getNumberOfOrder() {return numberOfOrder;    }
    public LocalDate getDateOfOrder() {return dateOfOrder;    }
    public OrderStatus getOrderStatus() {return orderStatus;    }
    public BigDecimal getTotalCost() {        return totalCost;    }

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
    public void setNumberOfOrder(Long numberOfOrder) {        this.numberOfOrder = numberOfOrder;    }
    public void setDateOfOrder(LocalDate dateOfOrder) {        this.dateOfOrder = dateOfOrder;    }
    public void setOrderStatus(OrderStatus orderStatus) {        this.orderStatus = orderStatus;    }
    public void setTotalCost(BigDecimal totalCost) {        this.totalCost = totalCost;    }
}
