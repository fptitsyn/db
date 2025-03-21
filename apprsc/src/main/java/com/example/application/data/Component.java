package com.example.application.data;

import jakarta.persistence.*;

@Entity
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long componentId;

    private String nameOfComponent;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryOfComponent category;

    private Double cost;

    // Геттеры и сеттеры
    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getNameOfComponent() {
        return nameOfComponent;
    }

    public void setNameOfComponent(String nameOfComponent) {
        this.nameOfComponent = nameOfComponent;
    }

    public CategoryOfComponent getCategory() {
        return category;
    }

    public void setCategory(CategoryOfComponent category) {
        this.category = category;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
