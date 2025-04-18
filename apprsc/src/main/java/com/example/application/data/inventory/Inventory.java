package com.example.application.data.inventory;

import com.example.application.data.components.Component;
import com.example.application.data.locations.Locations;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventory_id;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private Component component;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations locations;

    private int quantity;

    // Геттеры и сеттеры
    public Long getId() { return inventory_id; }
    public void setId(Long id) { this.inventory_id = id; }
    public Component getComponent() { return component; }
    public void setComponent(Component component) { this.component = component; }
    public Locations getLocations() { return locations; }
    public void setLocations(Locations locations) { this.locations = locations; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}