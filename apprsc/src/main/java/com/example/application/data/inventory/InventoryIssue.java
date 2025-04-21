package com.example.application.data.inventory;

import com.example.application.data.components.Component;
import com.example.application.data.locations.Locations;

import com.example.application.data.orders.Orders;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_issue")
public class InventoryIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issue_id;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders order;


    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations locations;

    private int quantity;
    private LocalDate issueDate;

    public Orders getOrder() {return order;}

    public void setOrder(Orders order) {this.order = order;}

    public Long getId() {
        return issue_id;
    }

    public void setId(Long issue_id) {
        this.issue_id = issue_id;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}