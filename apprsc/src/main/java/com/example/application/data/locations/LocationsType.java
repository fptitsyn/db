package com.example.application.data.locations;

import jakarta.persistence.*;

@Entity
@Table(name = "locations_type")
public class LocationsType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationTypeId;

    private String locationTypeName;

    // Геттеры
    public Long getLocationTypeId() {
        return locationTypeId;
    }

    public String getLocationTypeName() {
        return locationTypeName;
    }

    // Сеттеры
    public void setLocationTypeId(Long locationTypeId) {
        this.locationTypeId = locationTypeId;
    }

    public void setLocationTypeName(String locationTypeName) {
        this.locationTypeName = locationTypeName;
    }
}