package com.example.application.reports.locations;

import jakarta.persistence.*;

import java.time.LocalDate;

@SqlResultSetMapping(
        name = "LocationsInfoMapping",
        classes = @ConstructorResult(
                targetClass = LocationsInfoDTO.class,
                columns = {
                        @ColumnResult(name = "location_name", type = String.class),
                        @ColumnResult(name = "phone_number", type = String.class),
                        @ColumnResult(name = "country", type = String.class),
                        @ColumnResult(name = "city", type = String.class),
                        @ColumnResult(name = "street", type = String.class),
                        @ColumnResult(name = "building_number", type = String.class),
                        @ColumnResult(name = "postal_code", type = String.class),
                        @ColumnResult(name = "location_type_name", type = String.class)
                }
        )
)
@Entity
public class LocationsMappingConfig {
    @Id
    private Long id;
}
