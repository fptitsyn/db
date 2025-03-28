// CategoryOfComponent.java
package com.example.application.data.components;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class CategoryOfComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryOfComponentId;

    @ManyToOne
    @JoinColumn(name = "type_of_device_id")
    private TypeOfDevice typeOfDevice;

    @Column(nullable = false)
    @NotNull
    private String typeOfPartName;

    // Геттеры и сеттеры
    public Long getCategoryOfComponentId() {
        return categoryOfComponentId;
    }

    public void setCategoryOfComponentId(Long categoryOfComponentId) {
        this.categoryOfComponentId = categoryOfComponentId;
    }

    public TypeOfDevice getTypeOfDevice() {
        return typeOfDevice;
    }

    public void setTypeOfDevice(TypeOfDevice typeOfDevice) {
        this.typeOfDevice = typeOfDevice;
    }

    public String getTypeOfPartName() {
        return typeOfPartName;
    }

    public void setTypeOfPartName(String typeOfPartName) {
        this.typeOfPartName = typeOfPartName;
    }
}