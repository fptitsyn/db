package com.example.application.data.orders;

import com.example.application.data.components.Component;
import jakarta.persistence.*;

@Entity
@Table(name = "order_components")
public class OrderComponents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_components_id;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    public Long getId() {        return order_components_id;    }

    public void setId(Long order_components_id) {        this.order_components_id = order_components_id;    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
