package com.example.application.views.employees;

import com.example.application.data.employees.StaffingTable;
import com.example.application.data.employees.StaffingTableRepository;
import com.example.application.data.locations.Locations;
import com.example.application.data.locations.LocationsRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@PageTitle("Штатное расписание")
@Route("StaffingTable")
@SpringComponent
@Transactional
//@Menu(order = 41, icon = LineAwesomeIconUrl.FILE_ALT_SOLID)
@RolesAllowed({"HR", "GOD"})
public class StaffingTableForm extends VerticalLayout {
    private final LocationsRepository locationRepository;
    private final StaffingTableRepository staffingTableRepository;

    private final Grid<StaffingTable> grid = new Grid<>(StaffingTable.class);
    private final Binder<StaffingTable> binder = new Binder<>(StaffingTable.class);
    private FormLayout form;

    // Form fields
    private final TextField position = new TextField("Должность");
    private final TextField department = new TextField("Подразделение");

    private final BigDecimalField salary = new BigDecimalField("ФОТ");
    private final ComboBox<Locations> location = new ComboBox<>("Офис");
    private final Button saveButton = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());
    private final Button deleteButton = new Button("Удалить", VaadinIcon.TRASH.create());
    private final Button cancelButton = new Button("Отменить", VaadinIcon.CLOSE_CIRCLE_O.create());
    private final Button addButton = new Button("Новая", VaadinIcon.PLUS_SQUARE_O.create());
    private final Button backButton = new Button("Вернуться назад");

    public StaffingTableForm(StaffingTableRepository staffingTableRepository,
                             LocationsRepository locationRepository) {
        this.staffingTableRepository = staffingTableRepository;
        this.locationRepository = locationRepository;

        configureGrid();
        configureForm();

        add(addButton, grid, form);

        add(backButton);
        configureBackButton();

        updateGrid();
        setSizeFull();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        // Колонка Офис
        grid.addColumn(l -> {
                    Locations type = l.getLocation();
                    return type != null ? type.getName() : "—";
                })
                .setHeader("Офис")
                .setKey("office")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(st -> {
                    Locations loc = st.getLocation();
                    return loc != null ? loc.getName() : "";
                });

        // Колонка Подразделение
        grid.addColumn(StaffingTable::getDepartment)
                .setHeader("Подразделение")
                .setKey("department")
                .setAutoWidth(true)
                .setSortable(true);

        // Колонка Должность
        grid.addColumn(StaffingTable::getPosition)
                .setHeader("Должность")
                .setKey("position")
                .setAutoWidth(true)
                .setSortable(true);

        // Колонка ФОТ с валютой
        grid.addColumn(
                        new NumberRenderer<>(
                                StaffingTable::getSalary,
                                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
                        ))
                .setHeader("ФОТ")
                .setTextAlign(ColumnTextAlign.END)
                .setKey("salary");

        // Обработчик выбора записи
        grid.asSingleSelect().addValueChangeListener(e -> editStaffingTable(e.getValue()));

        // Установка сортировки по умолчанию
        Grid.Column<StaffingTable> officeColumn = grid.getColumnByKey("office");
        Grid.Column<StaffingTable> departmentColumn = grid.getColumnByKey("department");
        Grid.Column<StaffingTable> positionColumn = grid.getColumnByKey("position");

        List<GridSortOrder<StaffingTable>> sortOrder = Arrays.asList(
                new GridSortOrder<>(officeColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(departmentColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(positionColumn, SortDirection.ASCENDING)
        );
        grid.setMultiSort(true);
        grid.sort(sortOrder);
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
        saveButton.addClickListener(ignored -> saveStaffingTable());
        saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        deleteButton.addClickListener(ignored -> deleteStaffingTable());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        cancelButton.addClickListener(ignored -> cancelStaffingTable());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        addButton.addClickListener(ignored -> addStaffingTable());
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
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

    private void configureBackButton() {
        backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        backButton.addClickListener(ignored ->
                getUI().ifPresent(ui -> ui.navigate(HRView.class))
        );
    }

}

