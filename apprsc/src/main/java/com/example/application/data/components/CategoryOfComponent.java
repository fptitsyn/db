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
    public TypeOfDevice getTypeOfDevice() {
        return typeOfDevice;
    }
    public String getTypeOfPartName() {
        return typeOfPartName;
    }

    public String getComponentTypeCategory() { return typeOfDevice.getTypeOfDeviceName() + ">"+typeOfPartName;}

    public void setTypeOfDevice(TypeOfDevice typeOfDevice) {
        this.typeOfDevice = typeOfDevice;
    }
    public void setTypeOfPartName(String typeOfPartName) {
        this.typeOfPartName = typeOfPartName;
    }


}