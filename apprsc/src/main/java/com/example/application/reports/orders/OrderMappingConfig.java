package com.example.application.reports.orders;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@SqlResultSetMapping(
        name = "OrderInfoMapping",
        classes = @ConstructorResult(
                targetClass = OrderInfoDTO.class,
                columns = {
                        @ColumnResult(name = "location_name", type = String.class),
                        @ColumnResult(name = "full_name", type = String.class),
                        @ColumnResult(name = "number_of_order", type = Long.class),
                        @ColumnResult(name = "date_of_work_order", type = LocalDate.class),
                        @ColumnResult(name = "total_cost", type = BigDecimal.class),
                        @ColumnResult(name = "discounted_cost", type = BigDecimal.class),
                        @ColumnResult(name = "deducted_bonuses", type = BigDecimal.class),
                        @ColumnResult(name = "accrued_bonuses", type = BigDecimal.class),
                        @ColumnResult(name = "order_status", type = String.class)
                }
        )
)
@Entity
public class OrderMappingConfig { // Фиктивный класс для маппинга
    @Id
    private Long id;
}