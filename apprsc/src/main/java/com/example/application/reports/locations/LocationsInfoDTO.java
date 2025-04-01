package com.example.application.reports.locations;

import com.example.application.data.LocationsType;

import java.time.LocalDate;

public record LocationsInfoDTO (
    String location_name,
    String phone_number,
    String country,
    String city,
    String street,
    String building_number,
    String postal_code,
    String location_type_name){
        // Конструктор для JPA
    public LocationsInfoDTO(
            String location_name,
            String phone_number,
            String country,
            String city,
            String street,
            String building_number,
            String postal_code,
            String location_type_name
    )
    {
        this.location_type_name = location_type_name;
        this.location_name = location_name;
        this.phone_number = phone_number;
        this.street = street;
        this.country = country;
        this.postal_code = postal_code;
        this.city = city;
        this.building_number = building_number;
    }
}
