package com.example.application.views.orders;

import com.example.application.data.components.Component;
import com.example.application.data.orders.*;
import com.example.application.data.services.Services;
import com.example.application.reports.schedule.ScheduleData;
import com.example.application.reports.schedule.ScheduleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;

import java.util.List;

public class WorkOrderForm extends VerticalLayout {
    private final WorkOrders workOrder;
    private final OrdersService orderService;
    private final ScheduleService scheduleService;
    private final OrderServicesService orderServicesService;
    private final OrderComponentsService orderComponentsService;
    private final Runnable onProceed;
    private final Runnable onCancel;

    public WorkOrderForm(WorkOrders workOrder,
                         OrdersService orderService, ScheduleService scheduleService,
                         OrderServicesService orderServicesService,
                         OrderComponentsService orderComponentsService,
                         Runnable onProceed,
                         Runnable onCancel) {
        this.workOrder = workOrder;
        this.orderService = orderService;
        this.scheduleService = scheduleService;
        this.orderServicesService = orderServicesService;
        this.orderComponentsService = orderComponentsService;

        this.onProceed = onProceed;
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

        // Schedule grid
        Grid<ScheduleData> scheduleGrid = createScheduleGrid();
        Div scheduleContainer = new Div();
        scheduleContainer.setWidthFull();
        scheduleContainer.add(new Span("График"));
        scheduleContainer.add(scheduleGrid);

        // Buttons
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidthFull();
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonsLayout.setPadding(true);

        String buttonText = switch (workOrder.getWorkOrderStatus().getId().intValue()) {
            case 1 -> "Взять в работу";
            case 2 -> "Наряд выполнен";
            default -> "Ошибка!";
        };

        Button saveBtn = new Button(buttonText, VaadinIcon.CHECK.create(), ignored -> onProceed.run());
        styleButton(saveBtn, "primary");

        Button cancelBtn = new Button("Закрыть", VaadinIcon.CLOSE.create(), ignored -> onCancel.run());
        styleButton(cancelBtn, "error");

        if (buttonText.equals("Ошибка!")) {
            buttonsLayout.add(cancelBtn);
        } else {
            buttonsLayout.add(saveBtn, cancelBtn);
        }

        // Main layout
        add(buttonsLayout, headerLayout, scheduleContainer, servicesContainer, componentsContainer);
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

    private Grid<ScheduleData> createScheduleGrid() {
        Grid<ScheduleData> grid = new Grid<>(ScheduleData.class);
        grid.removeAllColumns();
        grid.setWidthFull();
        grid.setHeight("150px");

        // Получаем данные расписания
        List<ScheduleData> scheduleData = scheduleService.getScheduleByWorkOrderId(workOrder.getId());

        // Добавляем колонку с именем сотрудника
        grid.addColumn(ScheduleData::getEmployeeName)
                .setHeader("Сотрудник")
                .setWidth("250px")
                .setResizable(true);

        // Динамически добавляем только занятые интервалы
        addTimeColumnIfOccupied(grid, "09:00 - 10:00", ScheduleData::getTime09_10, scheduleData);
        addTimeColumnIfOccupied(grid, "10:00 - 11:00", ScheduleData::getTime10_11, scheduleData);
        addTimeColumnIfOccupied(grid, "11:00 - 12:00", ScheduleData::getTime11_12, scheduleData);
        addTimeColumnIfOccupied(grid, "12:00 - 13:00", ScheduleData::getTime12_13, scheduleData);
        addTimeColumnIfOccupied(grid, "14:00 - 15:00", ScheduleData::getTime14_15, scheduleData);
        addTimeColumnIfOccupied(grid, "15:00 - 16:00", ScheduleData::getTime15_16, scheduleData);
        addTimeColumnIfOccupied(grid, "16:00 - 17:00", ScheduleData::getTime16_17, scheduleData);
        addTimeColumnIfOccupied(grid, "17:00 - 18:00", ScheduleData::getTime17_18, scheduleData);

        // Добавляем колонку с общим количеством занятых часов
        grid.addColumn(ScheduleData::getTotal)
                .setHeader("Занято часов")
                .setTextAlign(ColumnTextAlign.CENTER);

        grid.setMultiSort(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setItems(scheduleData);

        return grid;
    }

    private void addTimeColumnIfOccupied(Grid<ScheduleData> grid, String header,
                                         ValueProvider<ScheduleData, Integer> valueProvider,
                                         List<ScheduleData> scheduleData) {
        // Проверяем, есть ли хотя бы одна запись с занятым временем в этом интервале
        boolean isOccupied = scheduleData.stream()
                .anyMatch(data -> valueProvider.apply(data) == 1);

        if (isOccupied) {
            grid.addColumn(item -> convertStatus(valueProvider.apply(item)))
                    .setHeader(header)
                    .setTextAlign(ColumnTextAlign.CENTER);
        }
    }

    private String convertStatus(int status) {
        return status == 0 ? "Свободно" : "Занято";
    }
}