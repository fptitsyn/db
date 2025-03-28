// Component.java
package com.example.application.data.components;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long componentId;

    @Column(nullable = false)
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryOfComponent category;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    // Геттеры и сеттеры
    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryOfComponent getCategory() {
        return category;
    }

    public void setCategory(CategoryOfComponent category) {
        this.category = category;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}