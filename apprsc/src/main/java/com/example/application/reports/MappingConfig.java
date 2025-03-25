package com.example.application.reports;

import jakarta.persistence.*;

import java.time.LocalDate;

@SqlResultSetMapping(
        name = "EmployeeInfoMapping",
        classes = @ConstructorResult(
                targetClass = EmployeeInfoDTO.class,
                columns = {
                        @ColumnResult(name = "last_name", type = String.class),
                        @ColumnResult(name = "first_name", type = String.class),
                        @ColumnResult(name = "middle_name", type = String.class),
                        @ColumnResult(name = "date_of_birth", type = LocalDate.class),
                        @ColumnResult(name = "phone_number", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "comment", type = String.class),
                        @ColumnResult(name = "age", type = Integer.class),
                        @ColumnResult(name = "position", type = String.class),
                        @ColumnResult(name = "salary", type = Integer.class),
                        @ColumnResult(name = "workplace", type = String.class),
                        @ColumnResult(name = "experience", type = Integer.class)
                }
        )
)
@Entity
public class MappingConfig { // Фиктивный класс для маппинга
    @Id
    private Long id;
}