package com.example.application.data.components;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class TypeOfDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeOfDeviceId;

    @Column(unique = true, nullable = false)
    @NotNull
    private String typeOfDeviceName;

    // Геттеры и сеттеры
    public Long getTypeOfDeviceId() {
        return typeOfDeviceId;
    }

    public void setTypeOfDeviceId(Long typeOfDeviceId) {
        this.typeOfDeviceId = typeOfDeviceId;
    }

    public String getTypeOfDeviceName() {
        return typeOfDeviceName;
    }

    public void setTypeOfDeviceName(String typeOfDeviceName) {
        this.typeOfDeviceName = typeOfDeviceName;
    }
}