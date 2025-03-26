package com.example.application.data;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "clients")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @OneToOne(mappedBy = "clients") // mappedBy указывает на поле в BonusAccount
    private BonusAccount bonusAccount;

    // Геттеры, сеттеры, конструкторы
    // Пустой конструктор (обязателен для JPA)
    public Clients() {
    }

    // Конструктор с параметром имени (опционально)
    public Clients(String name) {
        this.name = name;
    }

    // Геттеры
    public Long getId() {
        return client_id;
    }

    public String getName() {
        return name;
    }

    public List<Orders> getOrders() {
        return Collections.unmodifiableList(orders); // Защита от неконтролируемых изменений
    }
    public BonusAccount getBonusAccount() {
        return bonusAccount;
    }
    // Сеттеры
    public void setName(String name) {
        this.name = name;
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