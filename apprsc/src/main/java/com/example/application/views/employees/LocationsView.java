package com.example.application.views.employees;

import com.example.application.data.Locations;
import com.example.application.data.LocationsRepository;
import com.example.application.data.LocationsType;
import com.example.application.data.LocationTypeRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Офисы")
@Route("locations")

@Menu(order = 44, icon = LineAwesomeIconUrl.CITY_SOLID)
@RolesAllowed({"HR","GOD"})
public class LocationsView extends VerticalLayout {
    private final LocationsRepository locationRepository;
    private final LocationTypeRepository locationTypeRepository;

    private Grid<Locations> grid = new Grid<>(Locations.class);
    private Binder<Locations> binder = new Binder<>(Locations.class);
    private FormLayout form;

    // Form fields
    private TextField name = new TextField("Название");
    private TextField street = new TextField("Улица");
    private ComboBox<LocationsType> locationType = new ComboBox<>("Тип территории");
    private Button saveButton = new Button("Сохранить");
    private Button deleteButton = new Button("Удалить");
    private Button cancelButton = new Button("Отменить");
    private Button addButton = new Button("Добавить");

    public LocationsView(LocationsRepository locationRepository,
                         LocationTypeRepository locationTypeRepository) {
        this.locationRepository = locationRepository;
        this.locationTypeRepository = locationTypeRepository;

        configureGrid();
        configureForm();

        add(addButton, grid, form);
        updateGrid();
        setSizeFull();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(Locations::getName)
                .setHeader("Название")
                .setAutoWidth(true);

        grid.addColumn(Locations::getStreet)
                .setHeader("Улица")
                .setAutoWidth(true);

        grid.addColumn(l -> {
                    LocationsType type = l.getLocationType();
                    return type != null ? type.getLocationTypeName() : "—";
                })
                .setHeader("Тип")
                .setAutoWidth(true);

        grid.asSingleSelect().addValueChangeListener(e -> editLocation(e.getValue()));
    }

    private void configureForm() {
        form = new FormLayout();
        binder.setBean(new Locations());

        // Configure combo box
        locationType.setItems(locationTypeRepository.findAll());
        locationType.setItemLabelGenerator(LocationsType::getLocationTypeName);
        locationType.setRequired(true);

        // Configure binder
        binder.forField(name)
                .asRequired("Обязательное поле")
                .bind(Locations::getName, Locations::setName);

        binder.forField(street)
                .asRequired("Обязательное поле")
                .bind(Locations::getStreet, Locations::setStreet);

        binder.forField(locationType)
                .asRequired("Выберите тип")
                .bind(Locations::getLocationType, Locations::setLocationType);

        // Buttons layout
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);
        buttons.setSpacing(true);

        form.add(name, street, locationType, buttons);
        form.setVisible(false);

        // Event handlers
        saveButton.addClickListener(e -> saveLocation());
        deleteButton.addClickListener(e -> deleteLocation());
        cancelButton.addClickListener(e -> cancelLocation());
        addButton.addClickListener(e -> addLocation());
    }

    private void updateGrid() {
        grid.setItems(locationRepository.findAll());
    }

    private void editLocation(Locations location) {
        if (location == null) {
            hideForm();
        } else {
            binder.setBean(location);
            showForm();
        }
    }

    private void showForm() {
        form.setVisible(true);
        addButton.setVisible(false);
        name.focus();
    }

    private void hideForm() {
        form.setVisible(false);
        addButton.setVisible(true);
        binder.setBean(null);
    }

    private void addLocation() {
        grid.asSingleSelect().clear();
        binder.setBean(new Locations());
        showForm();
    }

    private void saveLocation() {
        if (binder.validate().isOk()) {
            locationRepository.save(binder.getBean());
            updateGrid();
            hideForm();
        }
    }
    private void deleteLocation() {
        Locations location = binder.getBean();
        if (location != null && location.getId() != null) {
            locationRepository.delete(location);
            updateGrid();
            hideForm();
        }
    }
    private void cancelLocation() {
        Locations location = binder.getBean();
        if (location != null && location.getId() != null) {
            hideForm();
        }
    }
}
