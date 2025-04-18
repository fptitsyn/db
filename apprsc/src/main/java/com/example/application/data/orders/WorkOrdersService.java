package com.example.application.data.orders;

import com.example.application.data.components.Component;
import com.example.application.data.employees.Employees;
import com.example.application.data.employees.Schedule;
import com.example.application.data.inventory.InventoryIssueService;
import com.example.application.data.locations.Locations;
import com.example.application.reports.schedule.ScheduleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WorkOrdersService {
    private final WorkOrderRepository repository;
    private final ScheduleService scheduleService;
    private final InventoryIssueService inventoryIssueService;
    private final OrderComponentsService orderComponentsService;

    public WorkOrdersService(WorkOrderRepository repository,
                             ScheduleService scheduleService,
                             InventoryIssueService inventoryIssueService,
                             OrderComponentsService orderComponentsService) {
        this.repository = repository;
        this.scheduleService = scheduleService;
        this.inventoryIssueService = inventoryIssueService;
        this.orderComponentsService = orderComponentsService;
    }

    public List<WorkOrders> findAll() {
        return repository.findAll();
    }

    public WorkOrders save(WorkOrders workOrders) {
        return repository.save(workOrders);
    }

    public void delete(WorkOrders workOrders) {
        repository.delete(workOrders);
    }

    @Transactional
    public void createWorkOrderWithInventoryIssue(Orders order,
                                                  Employees employee,
                                                  Set<Schedule> slots,
                                                  Locations location) {
        // 1. Создаем WorkOrder
        WorkOrders workOrder = new WorkOrders();
        workOrder.setOrders(order);
        workOrder.setEmployee(employee);
        repository.save(workOrder);

        // 2. Обновляем слоты расписания
        slots.forEach(slot -> {
            slot.setWorkOrders(workOrder);
            scheduleService.save(slot);
        });

        // 3. Списываем компоненты
        List<OrderComponents> components = orderComponentsService.findByOrderId(order.getId());
        for (OrderComponents oc : components) {
            inventoryIssueService.issueComponent(
                    oc.getComponent().getComponentId(),
                    location.getId(),
                    1
            );
        }
    }
}

