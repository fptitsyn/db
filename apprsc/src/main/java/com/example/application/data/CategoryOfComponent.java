package com.example.application.data;

import jakarta.persistence.*;

@Entity
public class CategoryOfComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryOfComponentId;

    @ManyToOne
    @JoinColumn(name = "type_of_device_id", nullable = false)
    private TypeOfDevice typeOfDevice;

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

    public String getTypeName() {
        return typeOfPartName;
    }

    public void setTypeName(String typeName) {
        this.typeOfPartName = typeName;
    }
}
