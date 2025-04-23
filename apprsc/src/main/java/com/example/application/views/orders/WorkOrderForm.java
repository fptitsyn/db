package com.example.application.views.orders;

import com.example.application.data.components.Component;
import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.employees.Schedule;
import com.example.application.data.locations.Locations;
import com.example.application.data.locations.LocationsService;
import com.example.application.data.orders.*;
import com.example.application.data.services.Services;
import com.example.application.reports.schedule.ScheduleData;
import com.example.application.reports.schedule.ScheduleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class WorkOrderForm extends VerticalLayout {
    private final WorkOrders workOrder;
    private final WorkOrdersService workOrdersService;
    private final ScheduleService scheduleService;
    private final OrderServicesService orderServicesService;
    private final OrderComponentsService orderComponentsService;
    private final LocationsService locationsService;
    private final EmployeesService employeesService;
    private final Runnable onProceed;
    private final Runnable onCancel;

    private Grid<ScheduleData> scheduleGrid;
    private VerticalLayout scheduleContainer;

    public WorkOrderForm(WorkOrders workOrder,
                         WorkOrdersService workOrdersService, ScheduleService scheduleService,
                         OrderServicesService orderServicesService,
                         OrderComponentsService orderComponentsService,
                         LocationsService locationsService,
                         EmployeesService employeesService,
                         Runnable onProceed,
                         Runnable onCancel) {
        this.workOrder = workOrder;
        this.workOrdersService = workOrdersService;
        this.scheduleService = scheduleService;
        this.orderServicesService = orderServicesService;
        this.orderComponentsService = orderComponentsService;
        this.locationsService = locationsService;
        this.employeesService = employeesService;
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
        servicesContainer.add(new Span("Работы"));
        servicesContainer.add(servicesGrid);

        // Components Grid
        Grid<Component> componentsGrid = createComponentsGrid();
        Div componentsContainer = new Div();
        componentsContainer.setWidthFull();
        componentsContainer.add(new Span("Комплектующие"));
        componentsContainer.add(componentsGrid);

        // Schedule grid
        scheduleGrid = createScheduleGrid();
        scheduleContainer = new VerticalLayout();
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

        Button ChangeMasterBtn = new Button("Сменить мастера", VaadinIcon.REFRESH.create(), ignored -> openChangeMasterDialog());
        styleButton(saveBtn, "primary");
        styleButton(ChangeMasterBtn, "primary");

        if (buttonText.equals("Ошибка!")) {
            buttonsLayout.add(cancelBtn);
        } else {
            buttonsLayout.add(ChangeMasterBtn, saveBtn);
        }

        // Main layout
        add(buttonsLayout, headerLayout, scheduleContainer, servicesContainer, componentsContainer);
        add(
                new HorizontalLayout(cancelBtn) {{
                    setWidthFull();
                    setJustifyContentMode(JustifyContentMode.END);
                }});
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
        grid.setHeight("250px");

        grid.addColumn(Services::getServiceName)
                .setHeader("Наименование")
                .setAutoWidth(true)
                .setFlexGrow(1);

        grid.addColumn(Services::getTimeToCompleteHours)
                .setHeader("Время выполнения")
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
        grid.setHeight("150px");

        grid.addColumn(c -> c.getCategory().getTypeOfDevice().getTypeOfDeviceName())
                .setHeader("Устройство")
                .setAutoWidth(true)
                .setFlexGrow(1);
        grid.addColumn(c -> c.getCategory().getTypeOfPartName())
                .setHeader("Тип")
                .setAutoWidth(true)
                .setFlexGrow(1);

        grid.addColumn(Component::getName)
                .setHeader("Наименование")
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
        grid.setHeight("100px");

        // Получаем данные расписания
        List<ScheduleData> scheduleData = scheduleService.getScheduleDataByWorkOrderId(workOrder.getId());

        // Добавляем колонку с именем сотрудника
        grid.addColumn(ScheduleData::getEmployeeName)
                .setHeader("Сотрудник")
                .setWidth("250px")
                .setResizable(true);

        grid.addColumn(ScheduleData::getWorkDay)
                .setHeader("Дата")
                .setWidth("50px")
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

    private void refreshScheduleGrid() {
        // Удаляем старый Grid и добавляем новый
        scheduleContainer.remove(scheduleGrid);
        scheduleGrid = createScheduleGrid();
        scheduleContainer.add(scheduleGrid);
    }

    // Методы смены мастера
    private void openChangeMasterDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Смена мастера");
        dialog.setWidth("800px");

        // Элементы управления
        ComboBox<Locations> locationComboBox = new ComboBox<>("Офис");
        ComboBox<Employees> employeeComboBox = new ComboBox<>("Сотрудник");
        DatePicker datePicker = new DatePicker("Дата работ");
        Grid<Schedule> scheduleChangeGrid = new Grid<>(Schedule.class);
        Button transferButton = new Button("Сменить мастера", VaadinIcon.REFRESH.create());
        Span warningSpan = new Span(); // Добавляем Span для сообщения
        warningSpan.getStyle().setColor("red");
        warningSpan.setVisible(false);

        // Настройка компонентов
        locationComboBox.setItems(locationsService.findAll());
        locationComboBox.setItemLabelGenerator(Locations::getName);

        List<ScheduleData> scheduleData = scheduleService.getScheduleDataByWorkOrderId(workOrder.getId());

        String currentEmployeeName = scheduleData.getFirst().getEmployeeName();

        employeeComboBox.setEnabled(false);
        employeeComboBox.setItemLabelGenerator(e -> e.getLastName() + " " + e.getFirstName());

        datePicker.setEnabled(false);
        datePicker.setMin(LocalDate.now());

        // Настройка Grid
        scheduleChangeGrid.removeAllColumns();
        scheduleChangeGrid.addColumn(Schedule::getTimeInterval).setHeader("Временной интервал");
        scheduleChangeGrid.addColumn(s -> s.getLocation().getName()).setHeader("Локация");
        scheduleChangeGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        scheduleChangeGrid.setEmptyStateText("Нет свободного времени");

        int totalTime = scheduleData.getFirst().getTotal();

        // Логика взаимодействия
        locationComboBox.addValueChangeListener(e -> {
            Locations loc = e.getValue();
            employeeComboBox.setEnabled(loc != null);
            List<Employees> filteredEmployees = employeesService.getOrderEmployeesByLocation(workOrder.getOrders().getId(), loc.getId()).stream()
                    .filter(emp -> !(emp.getLastName() + " " + emp.getFirstName()).equals(currentEmployeeName))
                    .toList();
            employeeComboBox.setItems(filteredEmployees);
        });

        employeeComboBox.addValueChangeListener(e -> {
            datePicker.setEnabled(e.getValue() != null);
        });

        datePicker.addValueChangeListener(e -> {
            if (e.getValue() != null && employeeComboBox.getValue() != null) {
                List<Schedule> slots = scheduleService.findAvailableSlots(
                        employeeComboBox.getValue().getId(),
                        e.getValue()
                );
                scheduleChangeGrid.setItems(slots);
            }
        });

        // Обработчик выбора слотов
        scheduleChangeGrid.addSelectionListener(event -> {
            int selectedCount = event.getAllSelectedItems().size();
            if (totalTime > 0 && selectedCount != totalTime) {
                warningSpan.setText("Необходимо выбрать слотов на " + totalTime + " часа");
                warningSpan.setVisible(true);
                transferButton.setEnabled(false);
            } else {
                warningSpan.setVisible(false);
                transferButton.setEnabled(true);
            }
        });

        transferButton.addClickListener(e -> {
            Set<Schedule> selectedSlots = scheduleChangeGrid.getSelectedItems();

            // Проверка на минимальное количество слотов

            if (totalTime > 0 && selectedSlots.size() < totalTime) {
                Notification.show("Требуется выбрать минимум " + totalTime + " слотов!",
                        3000, Notification.Position.MIDDLE);
                return;
            }

            if (selectedSlots.isEmpty()) {
                Notification.show("Выберите хотя бы один временной слот!");
                return;
            }

            try {
                // Очистка
                Set<Schedule> prevSchedule = scheduleService.getSchedule(workOrder.getId());
                prevSchedule.forEach(slot -> {
                    slot.setWorkOrders(null);
                    scheduleService.save(slot);
                });

                // Обновляем выбранные слоты расписания
                selectedSlots.forEach(slot -> {
                    workOrder.setEmployee(slot.getEmployee());
                    slot.setWorkOrders(workOrder);
                    scheduleService.save(slot);
                    workOrdersService.save(workOrder);
                });

                Notification.show("Заказ передан в работу! Выбрано слотов: " + selectedSlots.size());
                dialog.close();
                refreshScheduleGrid();
            } catch (Exception ex) {
                Notification.show("Ошибка: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Компоновка с добавлением предупреждения
        Button closeBtn = new Button("Отмена", VaadinIcon.CLOSE.create(), ev -> dialog.close());
        styleButton(closeBtn, "error");
        styleButton(transferButton, "primary");
        VerticalLayout layout = new VerticalLayout(
                new H2("Текущий Мастер:"),
                new Span(currentEmployeeName),
                new H2("Новый Мастер:"),
                new HorizontalLayout(locationComboBox, employeeComboBox, datePicker),
                scheduleChangeGrid,
                warningSpan, // Сообщение под Grid
                new HorizontalLayout(transferButton, closeBtn) {{
                    setWidthFull();
                    setJustifyContentMode(JustifyContentMode.END);
                }}
        );

        dialog.add(layout);
        dialog.open();
    }
}