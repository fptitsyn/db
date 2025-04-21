package com.example.application.views.employees;

import com.example.application.data.employees.EmployeeWithPositionDTO;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.locations.Locations;
import com.example.application.data.locations.LocationsService;
import com.example.application.reports.schedule.ScheduleData;
import com.example.application.reports.schedule.ScheduleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.LocalDate;

@Route(value = "schedule-view")
@PageTitle("График работы")
@Menu(order = 43, icon = LineAwesomeIconUrl.BUSINESS_TIME_SOLID)
@RolesAllowed({"HR", "SALES", "WORKS", "GOD"})
public class ScheduleView extends VerticalLayout {

    private final DatePicker datePicker = new DatePicker("Дата");
    private final ComboBox<Locations> locationComboBox = new ComboBox<>("Офис");
    private final Grid<ScheduleData> grid = new Grid<>();
    private final Button addButton = new Button("Добавить/удалить график", VaadinIcon.PLUS_SQUARE_O.create());

    private final LocationsService locationsService;
    private final EmployeesService employeesService;
    private final ScheduleService scheduleService;

    public ScheduleView(LocationsService locationsService,
                        EmployeesService employeesService,
                        ScheduleService scheduleService) {
        this.locationsService = locationsService;
        this.employeesService = employeesService;
        this.scheduleService = scheduleService;

        configureComponents();
        configureGrid();
        configureAddButton();
        updateGrid();

        add(new HorizontalLayout(datePicker, locationComboBox), addButton, grid);
    }

    private void configureComponents() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        datePicker.setValue(LocalDate.now());
        datePicker.addValueChangeListener(ignored -> updateGrid());

        locationComboBox.setItemLabelGenerator(Locations::getName);
        locationComboBox.setItems(locationsService.findAll());
        locationComboBox.setPlaceholder("Выберите офис");
        locationComboBox.addValueChangeListener(ignored -> updateGrid());
        styleButton(addButton, "primary");
    }

    private void configureGrid() {
        grid.addColumn(ScheduleData::getEmployeeName)
                .setHeader("Сотрудник")
                .setWidth("250px")
                .setResizable(true);

        addTimeColumn("09:00 - 10:00", ScheduleData::getTime09_10);
        addTimeColumn("10:00 - 11:00", ScheduleData::getTime10_11);
        addTimeColumn("11:00 - 12:00", ScheduleData::getTime11_12);
        addTimeColumn("12:00 - 13:00", ScheduleData::getTime12_13);
        addTimeColumn("14:00 - 15:00", ScheduleData::getTime14_15);
        addTimeColumn("15:00 - 16:00", ScheduleData::getTime15_16);
        addTimeColumn("16:00 - 17:00", ScheduleData::getTime16_17);
        addTimeColumn("17:00 - 18:00", ScheduleData::getTime17_18);

        grid.addColumn(ScheduleData::getTotal)
                .setHeader("Занято часов")
                .setTextAlign(ColumnTextAlign.CENTER);

        grid.setHeight("600px");
        grid.setMultiSort(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setEmptyStateText("Доступных графиков на эту дату в данном офисе - нет!");
    }

    private void addTimeColumn(String header, ValueProvider<ScheduleData, Integer> valueProvider) {
        grid.addColumn(item -> convertStatus(valueProvider.apply(item)))
                .setHeader(header)
                .setTextAlign(ColumnTextAlign.CENTER);
    }

    private String convertStatus(int status) {
        return status == 0 ? "Свободно" : "Занято";
    }

    private void configureAddButton() {
        addButton.addClickListener(ignored -> showScheduleForm());
    }

    private void showScheduleForm() {
        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();

        DatePicker dateField = new DatePicker("Дата");
        ComboBox<Locations> locationField = new ComboBox<>("Офис");
        ComboBox<EmployeeWithPositionDTO> employeeField = new ComboBox<>("Сотрудник");

        dateField.setValue(LocalDate.now());
        locationField.setItemLabelGenerator(Locations::getName);
        locationField.setItems(locationsService.findAll());

        locationField.addValueChangeListener(event -> {
            Locations selectedLocation = event.getValue();
            if (selectedLocation != null) {
                employeeField.setItems(
                        employeesService.findEmployeesWithPositionByLocation(selectedLocation.getId())
                );
            } else {
                employeeField.clear();
            }
        });

        employeeField.setItemLabelGenerator(EmployeeWithPositionDTO::getFullNameWithPosition);
        employeeField.setEnabled(false);

        locationField.addValueChangeListener(event -> {
            employeeField.setEnabled(event.getValue() != null);
            if (event.getValue() == null) {
                employeeField.clear();
            }
        });

        Button addScheduleButton = new Button("Добавить график", VaadinIcon.PLUS.create());
        Button deleteScheduleButton = new Button("Удалить график", VaadinIcon.TRASH.create());
        Button cancelButton = new Button("Отмена", VaadinIcon.CLOSE_CIRCLE_O.create(), ignored -> dialog.close());

        styleButton(addScheduleButton, "primary");
        styleButton(deleteScheduleButton, "primary");
        styleButton(cancelButton, "error");

        addScheduleButton.addClickListener(ignored -> {
            try {
                handleAddSchedule(
                        dateField.getValue(),
                        locationField.getValue(),
                        employeeField.getValue()
                );
                dialog.close();
                updateGrid();
            } catch (Exception ex) {
                handleScheduleError(ex);
            }
        });

        deleteScheduleButton.addClickListener(ignored -> {
            try {
                handleDeleteSchedule(
                        dateField.getValue(),
                        locationField.getValue(),
                        employeeField.getValue()
                );
                dialog.close();
                updateGrid();
            } catch (Exception ex) {
                handleScheduleError(ex);
            }
        });

        form.add(dateField, locationField, employeeField);
        dialog.add(form,
                new HorizontalLayout(addScheduleButton, deleteScheduleButton, cancelButton) {{
                    setWidthFull();
                    setJustifyContentMode(JustifyContentMode.END);
        }});
        dialog.open();
    }

    private void handleAddSchedule(LocalDate date, Locations location, EmployeeWithPositionDTO employee) {
        if (date == null || location == null || employee == null) {
            throw new IllegalArgumentException("Все поля должны быть заполнены!");
        }

        scheduleService.insertScheduleEntries(date, employee.getEmployeeId(), location.getId());
        Notification.show("График успешно добавлен", 3000, Notification.Position.TOP_CENTER);
    }

    private void handleDeleteSchedule(LocalDate date, Locations location, EmployeeWithPositionDTO employee) {
        if (date == null || location == null || employee == null) {
            throw new IllegalArgumentException("Все поля должны быть заполнены!");
        }

        scheduleService.deleteScheduleEntries(date, employee.getEmployeeId(), location.getId());
        Notification.show("График успешно удален", 3000, Notification.Position.TOP_CENTER);
    }

    private void handleScheduleError(Exception ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        String message = cause.getMessage();

        if (message.contains("Существуют связанные заказы")) {
            Notification.show("Невозможно удалить: есть связанные заказы",
                    5000, Notification.Position.TOP_CENTER);
        } else if (message.contains("Записи для удаления не найдены")) {
            Notification.show("Записи не найдены для указанных параметров",
                    3000, Notification.Position.TOP_CENTER);
        } else if (message.contains("DUPLICATE_SCHEDULE_ENTRY")) {
            Notification.show("График уже существует для выбранных параметров",
                    3000, Notification.Position.TOP_CENTER);
        } else {
            Notification.show("Ошибка: " + message,
                    5000, Notification.Position.TOP_CENTER);
        }
    }

    private void updateGrid() {
        LocalDate selectedDate = datePicker.getValue();
        Locations selectedLocation = locationComboBox.getValue();

        if (selectedDate != null && selectedLocation != null) {
            grid.setItems(
                    scheduleService.getSchedule(selectedDate, selectedLocation.getId())
            );
        }
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
}