package com.example.application.data.employees;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "locations")
public class Locations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_id_seq")
    //@SequenceGenerator(name = "location_id_seq", initialValue = 1)
    private Long location_id;


    private String name;
    private String phone_number;
    private String postal_code;
    private String country;
    private String city;
    private String street;
    private String building_number;

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

    public String getStreet() {
        return street;
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

    public void setStreet(String street) {
        this.street = street;
    }

    public void setLocationType(LocationsType locationType) {
        this.locationType = locationType;
    }
    public String getLocationTypeName() {
        return locationType != null ? locationType.getLocationTypeName() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locations location = (Locations) o;
        return Objects.equals(location_id, location.location_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location_id);
    }
}