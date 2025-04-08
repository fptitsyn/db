package com.example.application.data.orders;

import com.example.application.data.services.Services;
import jakarta.persistence.*;

@Entity
@Table(name = "order_services")
public class OrderServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_services_id;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;

    public Long getOrder_services_id() {
        return order_services_id;
    }

    public void setOrder_services_id(Long order_services_id) {
        this.order_services_id = order_services_id;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }
}
