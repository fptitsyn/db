package com.example.application.views.orders;

import com.example.application.data.components.ComponentService;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.orders.*;
import com.example.application.data.services.ServicesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class WorkOrderForm extends VerticalLayout {
    private final WorkOrders workOrder;
    private final OrdersService orderService;
    private final OrderServicesService orderServicesService;
    private final ServicesService servicesService;
    private final OrderComponentsService orderComponentsService;
    private final ComponentService componentService;
    private final WorkOrdersService workOrdersService;
    private final EmployeesService employeesService;
    private final Runnable onSave;
    private final Runnable onCancel;

    public WorkOrderForm(WorkOrders workOrder,
                         OrdersService orderService,
                         OrderServicesService orderServicesService,
                         ServicesService servicesService,
                         OrderComponentsService orderComponentsService,
                         ComponentService componentService,
                         WorkOrdersService workOrdersService,
                         EmployeesService employeesService,
                         Runnable onSave,
                         Runnable onCancel) {
        this.workOrder = workOrder;
        this.orderService = orderService;
        this.orderServicesService = orderServicesService;
        this.servicesService = servicesService;
        this.orderComponentsService = orderComponentsService;
        this.componentService = componentService;
        this.workOrdersService = workOrdersService;
        this.employeesService = employeesService;
        this.onSave = onSave;
        this.onCancel = onCancel;

        initForm();
    }

    private void initForm() {
        add(new HorizontalLayout(createSaveButton(), createCancelButton()));
    }

    private Button createSaveButton() {
        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK.create(), e -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return saveBtn;
    }

    private Button createCancelButton() {
        Button cancelBtn = new Button("Закрыть", VaadinIcon.CLOSE.create(), e -> onCancel.run());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return cancelBtn;
    }

    private void save() {
        onSave.run();
    }
}