package com.example.application.data.components;

import java.math.BigDecimal;

public class ComponentDTO {
    private Long componentId;
    private String typeOfDeviceName = "";
    private String typeOfPartName = "";
    private String componentName = "";
    private BigDecimal cost = BigDecimal.ZERO;

    // Конструктор по умолчанию (без аргументов)
    public ComponentDTO() {
    }

    // Конструктор для JPQL-запроса
    public ComponentDTO(
            Long componentId,
            String typeOfDeviceName,
            String typeOfPartName,
            String componentName,
            BigDecimal cost
    ) {
        this.componentId = componentId;
        this.typeOfDeviceName = typeOfDeviceName;
        this.typeOfPartName = typeOfPartName;
        this.componentName = componentName;
        this.cost = cost;
    }

    // Геттеры и сеттеры
    public Long getComponentId() { return componentId; }
    public void setComponentId(Long componentId) { this.componentId = componentId; }

    public String getTypeOfDeviceName() { return typeOfDeviceName; }
    public void setTypeOfDeviceName(String typeOfDeviceName) { this.typeOfDeviceName = typeOfDeviceName; }

    public String getTypeOfPartName() { return typeOfPartName; }
    public void setTypeOfPartName(String typeOfPartName) { this.typeOfPartName = typeOfPartName; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public BigDecimal getCost() {
        return cost != null ? cost : BigDecimal.ZERO;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost != null ? cost : BigDecimal.ZERO;
    }
}