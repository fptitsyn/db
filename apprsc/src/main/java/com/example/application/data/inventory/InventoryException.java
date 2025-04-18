package com.example.application.data.inventory;

public class InventoryException extends RuntimeException {
    private final String componentName;
    private final int requiredQuantity;

    public InventoryException(String componentName, int requiredQuantity) {
        super("Недостаточно компонента: " + componentName);
        this.componentName = componentName;
        this.requiredQuantity = requiredQuantity;
    }

    // Геттеры
    public String getComponentName() { return componentName; }
    public int getRequiredQuantity() { return requiredQuantity; }
}