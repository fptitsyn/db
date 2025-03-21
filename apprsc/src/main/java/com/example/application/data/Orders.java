package com.example.application.data;

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

    private String product;
    private int quantity;

    // Геттеры, сеттеры, конструкторы
    // Пустой конструктор (обязателен для JPA)
    public Orders() {
    }

    // Конструктор с основными параметрами (опционально)
    public Orders(Clients client, String product, int quantity) {
        this.client = client;
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

    public void setProduct(String product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}