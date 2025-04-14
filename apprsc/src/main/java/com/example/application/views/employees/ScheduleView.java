package com.example.application.views.employees;

import com.example.application.data.employees.Employees;
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
@RolesAllowed({"HR", "WORKS", "GOD"})
public class ScheduleView extends VerticalLayout {

    // Компоненты интерфейса
    private final DatePicker datePicker = new DatePicker("Дата");
    private final ComboBox<Locations> locationComboBox = new ComboBox<>("Офис");
    private final Grid<ScheduleData> grid = new Grid<>();
    private final Button addButton = new Button("Добавить/удалить график", VaadinIcon.PLUS.create());

    // Сервисы
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

        // Настройка поля даты
        datePicker.setValue(LocalDate.now());
        datePicker.addValueChangeListener(ignored -> updateGrid());

        // Настройка выбора офиса
        locationComboBox.setItemLabelGenerator(Locations::getName);
        locationComboBox.setItems(locationsService.findAll());
        locationComboBox.setPlaceholder("Выберите офис");
        locationComboBox.addValueChangeListener(ignored -> updateGrid());

        // Стиль кнопки
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
    }

    private void configureGrid() {
        // Настройка колонок
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

        // Общие настройки таблицы
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

        // Поля формы
        DatePicker dateField = new DatePicker("Дата");
        ComboBox<Locations> locationField = new ComboBox<>("Офис");
        ComboBox<Employees> employeeField = new ComboBox<>("Сотрудник");

        // Настройка полей
        dateField.setValue(LocalDate.now());
        locationField.setItemLabelGenerator(Locations::getName);
        locationField.setItems(locationsService.findAll());
        employeeField.setItemLabelGenerator(Employees::getFullName);
        employeeField.setItems(employeesService.findAll());

        // Кнопки формы
        Button addScheduleButton = new Button("Добавить график", VaadinIcon.PLUS.create());
        Button deleteScheduleButton = new Button("Удалить график", VaadinIcon.TRASH.create());
        Button cancelButton = new Button("Отмена", ignored -> dialog.close());

        // Стили кнопок
        addScheduleButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteScheduleButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

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
        HorizontalLayout buttonsLayout = new HorizontalLayout(
                addScheduleButton, deleteScheduleButton, cancelButton
        );
        buttonsLayout.setSpacing(true);

        dialog.add(form, buttonsLayout);
        dialog.open();
    }

    private void handleAddSchedule(LocalDate date, Locations location, Employees employee) {
        if (date == null || location == null || employee == null) {
            throw new IllegalArgumentException("Все поля должны быть заполнены!");
        }

        scheduleService.insertScheduleEntries(date, employee.getId(), location.getId());
        Notification.show("График успешно добавлен", 3000, Notification.Position.TOP_CENTER);
    }

    private void handleDeleteSchedule(LocalDate date, Locations location, Employees employee) {
        if (date == null || location == null || employee == null) {
            throw new IllegalArgumentException("Все поля должны быть заполнены!");
        }

        scheduleService.deleteScheduleEntries(date, employee.getId(), location.getId());
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
}