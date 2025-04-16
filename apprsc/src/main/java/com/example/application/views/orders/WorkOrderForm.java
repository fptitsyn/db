package com.example.application.views.orders;

import com.example.application.data.components.Component;
import com.example.application.data.components.ComponentService;
import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.orders.*;
import com.example.application.data.services.Services;
import com.example.application.data.services.ServicesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;

import java.util.List;

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
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Header with order info
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setPadding(true);
        headerLayout.getStyle().set("background", "var(--lumo-contrast-5pct)");
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField orderNumber = createReadOnlyField("Номер заказа",
                workOrder.getOrders() != null ? workOrder.getOrders().getNumberOfOrder().toString() : "");
        TextField workOrderNumber = createReadOnlyField("Номер наряда",
                workOrder.getNumberOfWorkOrder() != null ? workOrder.getNumberOfWorkOrder().toString() : "");
        TextField workOrderDate = createReadOnlyField("Дата наряда",
                workOrder.getDateOfWorkOrder() != null ? workOrder.getDateOfWorkOrder().toString() : "");


        headerLayout.add(orderNumber, workOrderNumber, workOrderDate);
        headerLayout.setFlexGrow(1, orderNumber, workOrderNumber, workOrderDate);

        // Services Grid
        Grid<Services> servicesGrid = createServicesGrid();
        Div servicesContainer = new Div();
        servicesContainer.setWidthFull();
        servicesContainer.add(new Span("Услуги"));
        servicesContainer.add(servicesGrid);

        // Components Grid
        Grid<Component> componentsGrid = createComponentsGrid();
        Div componentsContainer = new Div();
        componentsContainer.setWidthFull();
        componentsContainer.add(new Span("Компоненты"));
        componentsContainer.add(componentsGrid);

        // Buttons
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidthFull();
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonsLayout.setPadding(true);

        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK.create(), e -> onSave.run());
        styleButton(saveBtn, "primary");

        Button cancelBtn = new Button("Закрыть", VaadinIcon.CLOSE.create(), e -> onCancel.run());
        styleButton(cancelBtn, "error");

        buttonsLayout.add(saveBtn, cancelBtn);

        // Main layout
        add(headerLayout, servicesContainer, componentsContainer, buttonsLayout);
        setFlexGrow(1, servicesContainer, componentsContainer);
    }

    private void styleButton(Button button, String theme) {
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        if ("primary".equals(theme)) {
            button.getStyle().set("color", "var(--lumo-primary-text-color)");
        } else {
            button.getStyle().set("color", "var(--lumo-error-text-color)");
        }
        button.getStyle().set("margin-right", "1em");
    }

    private TextField createReadOnlyField(String label, String value) {
        TextField field = new TextField(label);
        field.setValue(value);
        field.setReadOnly(true);
        field.setWidthFull();
        return field;
    }

    private Grid<Services> createServicesGrid() {
        Grid<Services> grid = new Grid<>(Services.class);
        grid.removeAllColumns();
        grid.setWidthFull();
        grid.setHeight("300px");

        grid.addColumn(Services::getServiceName)
                .setHeader("Наименование")
                .setAutoWidth(true)
                .setFlexGrow(1);

        grid.addColumn(s -> String.format("%,.2f ₽", s.getCost()))
                .setHeader("Стоимость")
                .setTextAlign(ColumnTextAlign.END)
                .setWidth("120px");

        if (workOrder.getOrders() != null) {
            List<OrderServices> orderServices = orderServicesService.findByOrderId(workOrder.getOrders().getId());
            List<Services> services = orderServices.stream()
                    .map(OrderServices::getServices)
                    .toList();
            grid.setItems(services);
        }

        return grid;
    }

    private Grid<Component> createComponentsGrid() {
        Grid<Component> grid = new Grid<>(Component.class);
        grid.removeAllColumns();
        grid.setWidthFull();
        grid.setHeight("300px");

        grid.addColumn(Component::getName)
                .setHeader("Наименование")
                .setAutoWidth(true)
                .setFlexGrow(1);

        grid.addColumn(c -> c.getCategory().getTypeOfPartName())
                .setHeader("Тип")
                .setAutoWidth(true)
                .setFlexGrow(1);

        grid.addColumn(c -> c.getCategory().getTypeOfDevice().getTypeOfDeviceName())
                .setHeader("Устройство")
                .setAutoWidth(true)
                .setFlexGrow(1);

        grid.addColumn(c -> String.format("%,.2f ₽", c.getCost()))
                .setHeader("Стоимость")
                .setTextAlign(ColumnTextAlign.END)
                .setWidth("120px");

        if (workOrder.getOrders() != null) {
            List<OrderComponents> orderComponents = orderComponentsService.findByOrderId(workOrder.getOrders().getId());
            List<Component> components = orderComponents.stream()
                    .map(OrderComponents::getComponent)
                    .toList();
            grid.setItems(components);
        }

        return grid;
    }
}