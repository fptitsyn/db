package com.example.application.reports.workOrders;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@SqlResultSetMapping(
        name = "WorkOrderInfoMapping",
        classes = @ConstructorResult(
                targetClass = WorkOrderInfoDTO.class,
                columns = {
                        @ColumnResult(name = "full_name", type = String.class),
                        @ColumnResult(name = "number_of_work_order", type = Long.class),
                        @ColumnResult(name = "date_of_work_order", type = LocalDate.class),
                        @ColumnResult(name = "services_amount", type = Integer.class),
                        @ColumnResult(name = "hours_amount", type = Integer.class),
                        @ColumnResult(name = "order_status", type = String.class)
                }
        )
)
@Entity
public class WorkOrderMappingConfig { // Фиктивный класс для маппинга
    @Id
    private Long id;
}