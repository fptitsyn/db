package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TypeOfDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeOfDeviceId;

    private String typeOfDeviceName;

    // Геттеры и сеттеры
    public Long getTypeOfDeviceId() {
        return typeOfDeviceId;
    }

    public void setTypeOfDeviceId(Long typeOfDeviceId) {
        this.typeOfDeviceId = typeOfDeviceId;
    }

    public String getTypeName() {
        return typeOfDeviceName;
    }

    public void setTypeName(String typeName) {
        this.typeOfDeviceName = typeName;
    }
}