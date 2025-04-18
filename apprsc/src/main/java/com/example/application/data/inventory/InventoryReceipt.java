package com.example.application.data.inventory;

import com.example.application.data.components.Component;
import com.example.application.data.locations.Locations;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_receipt")
public class InventoryReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receipt_id;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations locations;

    private int quantity;
    private LocalDate receiptDate;

    public Long getId() {
        return receipt_id;
    }

    public void setId(Long receipt_id) {
        this.receipt_id = receipt_id;
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

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }
}