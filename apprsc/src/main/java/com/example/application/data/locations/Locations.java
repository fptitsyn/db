package com.example.application.data.locations;

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
    private Integer employeeAmount;
    private String phoneNumber;
    private String postalCode;
    private String country;
    private String city;
    private String street;
    private String buildingNumber;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public LocationsType getLocationType() {
        return locationType;
    }
    public Integer getEmployeeAmount() {
        return employeeAmount;
    }

    // Сеттеры
    public void setId(Long id) {
        this.location_id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phone_number) {
        this.phoneNumber = phone_number;
    }

    public void setPostalCode(String postal_code) {
        this.postalCode = postal_code;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBuildingNumber(String building_number) {
        this.buildingNumber = building_number;
    }

    public void setLocationType(LocationsType locationType) {
        this.locationType = locationType;
    }

    public String getLocationTypeName() {
        return locationType != null ? locationType.getLocationTypeName() : "";
    }
    public void setEmployeeAmount(Integer employeeAmount) {
        this.employeeAmount = employeeAmount;
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