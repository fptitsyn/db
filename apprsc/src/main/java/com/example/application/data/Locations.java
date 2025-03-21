package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Locations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_id_seq")
    //@SequenceGenerator(name = "location_id_seq", initialValue = 1)
    private Long location_id;


    private String name;
    private String address;

    @ManyToOne
    @JoinColumn(name = "location_type_id")
    private LocationsType locationType;

    // Геттеры
    public Long getId() {
        return location_id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LocationsType getLocationType() {
        return locationType;
    }

    // Сеттеры
    public void setId(Long id) {
        this.location_id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocationType(LocationsType locationType) {
        this.locationType = locationType;
    }
    public String getLocationTypeName() {
        return locationType != null ? locationType.getLocationTypeName() : "";
    }
}