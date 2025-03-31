package com.example.application.views.employees;

import com.example.application.data.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Штатное расписание")
@Route("StaffingTable")
@SpringComponent
@Transactional
@Menu(order = 41, icon = LineAwesomeIconUrl.FILE_ALT_SOLID)
@RolesAllowed({"HR","GOD"})
public class StaffingTableView extends VerticalLayout {
    private final LocationsRepository locationRepository;
    private final StaffingTableRepository staffingTableRepository;

    private Grid<StaffingTable> grid = new Grid<>(StaffingTable.class);
    private Binder<StaffingTable> binder = new Binder<>(StaffingTable.class);
    private FormLayout form;

    // Form fields
    private TextField position = new TextField("Должность");
    private TextField department = new TextField("Подразделение");

    private IntegerField salary = new IntegerField("ФОТ");
    private ComboBox<Locations> location = new ComboBox<>("Офис");
    private Button saveButton = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());
    private Button deleteButton = new Button("Удалить", VaadinIcon.TRASH.create());
    private Button cancelButton = new Button("Отменить", VaadinIcon.CLOSE_CIRCLE_O.create());
    private Button addButton = new Button("Новая", VaadinIcon.PLUS_SQUARE_O.create());

    public StaffingTableView(StaffingTableRepository staffingTableRepository,
                             LocationsRepository locationRepository) {
        this.staffingTableRepository = staffingTableRepository;
        this.locationRepository = locationRepository;

        configureGrid();
        configureForm();

        add(addButton, grid, form);
        updateGrid();
        setSizeFull();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(StaffingTable::getDepartment)
                .setHeader("Подразделение")

                .setAutoWidth(true);
        grid.addColumn(StaffingTable::getPosition)
                .setHeader("Должность")
                .setAutoWidth(true);

        grid.addColumn(StaffingTable::getSalary)
                .setHeader("ФОТ")
                .setAutoWidth(true);

        grid.addColumn(l -> {
                    Locations type = l.getLocation();
                    return type != null ? type.getName() : "—";
                })
                .setHeader("Офис")
                .setAutoWidth(true);

        grid.asSingleSelect().addValueChangeListener(e -> editStaffingTable(e.getValue()));
    }

    private void configureForm() {
        form = new FormLayout();
        binder.setBean(new StaffingTable());

        // Configure combo box
        location.setItems(locationRepository.findAll());
        location.setItemLabelGenerator(Locations::getName);
        location.setRequired(true);

        // Configure binder

        binder.forField(department)
                .asRequired("Обязательное поле")
                .bind(StaffingTable::getDepartment, StaffingTable::setDepartment);
        binder.forField(position)
                .asRequired("Обязательное поле")
                .bind(StaffingTable::getPosition, StaffingTable::setPosition);

        binder.forField(salary)
                .asRequired("Обязательное поле")
                .bind(StaffingTable::getSalary, StaffingTable::setSalary);

        binder.forField(location)
                .asRequired("Выберите тип")
                .bind(StaffingTable::getLocation, StaffingTable::setLocation);

        // Buttons layout
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);
        buttons.setSpacing(true);

        form.add(department, position, salary, location, buttons);
        form.setVisible(false);
        // Event handlers
        saveButton.addClickListener(e -> saveStaffingTable());
        deleteButton.addClickListener(e -> deleteStaffingTable());
        cancelButton.addClickListener(e -> cancelStaffingTable());
        addButton.addClickListener(e -> addStaffingTable());
    }

    private void updateGrid() {
        grid.setItems(staffingTableRepository.findAll());
    }

    private void editStaffingTable(StaffingTable staffingTable) {
        if (staffingTable == null) {
            hideForm();
        } else {
            // Получаем свежую версию из базы
            StaffingTable freshStaffingTable = staffingTableRepository.findById(staffingTable.getId()).orElse(null);
            binder.setBean(freshStaffingTable);
            showForm();

            // Обновляем список и устанавливаем значение
            if (freshStaffingTable != null) {
                location.setItems(locationRepository.findAll());
                location.setValue(freshStaffingTable.getLocation());
            }
        }
    }

    private void showForm() {
        location.setItems(locationRepository.findAll()); // Обновляем список каждый раз
        form.setVisible(true);
        addButton.setVisible(false);
        position.focus();
    }

    private void hideForm() {
        form.setVisible(false);
        addButton.setVisible(true);
        binder.setBean(null);
    }

    private void addStaffingTable() {
        grid.asSingleSelect().clear();
        binder.setBean(new StaffingTable());
        showForm();
    }

    private void saveStaffingTable() {
        if (binder.validate().isOk()) {
            staffingTableRepository.save(binder.getBean());
            updateGrid();
            hideForm();
        }
    }
    private void deleteStaffingTable() {
        StaffingTable staffingTable = binder.getBean();
        if (staffingTable != null && staffingTable.getId() != null) {
            staffingTableRepository.delete(staffingTable);
            updateGrid();
            hideForm();
        }
    }

    private void cancelStaffingTable() {
        StaffingTable staffingTable = binder.getBean();
        if (staffingTable != null && staffingTable.getId() != null) {
            hideForm();
        }
    }


}

