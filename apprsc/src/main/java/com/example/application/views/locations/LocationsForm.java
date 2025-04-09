package com.example.application.views.locations;

import com.example.application.data.employees.Employees;
import com.example.application.data.locations.Locations;
import com.example.application.data.locations.LocationsRepository;
import com.example.application.data.locations.LocationsType;
import com.example.application.data.locations.LocationTypeRepository;
import com.example.application.views.employees.HRView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;


@PageTitle("Офисы")
@Route("locations")

@RolesAllowed({"HR","GOD"})
public class LocationsForm extends VerticalLayout {
    private final LocationsRepository locationRepository;
    private final LocationTypeRepository locationTypeRepository;

    private Grid<Locations> grid = new Grid<>(Locations.class);
    private Binder<Locations> binder = new Binder<>(Locations.class);
    private FormLayout form;

    // Form fields
    private TextField name = new TextField("Название");
    private TextField phoneNumber = new TextField("Номер телефона");
    private TextField postalCode = new TextField("Почтовый индекс");
    private TextField country = new TextField("Страна");
    private TextField city = new TextField("Город");
    private TextField buildingNumber = new TextField("Номер строения");
    private TextField street = new TextField("Улица");
    private ComboBox<LocationsType> locationType = new ComboBox<>("Тип территории");
    private Button saveButton = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());
    private Button deleteButton = new Button("Удалить", VaadinIcon.TRASH.create());
    private Button cancelButton = new Button("Отменить", VaadinIcon.CLOSE_CIRCLE_O.create());
    private Button addButton = new Button("Новый", VaadinIcon.PLUS_SQUARE_O.create());
    private Button backButton = new Button("Вернуться назад");

    public LocationsForm(LocationsRepository locationRepository,
                         LocationTypeRepository locationTypeRepository) {
        this.locationRepository = locationRepository;
        this.locationTypeRepository = locationTypeRepository;

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

        grid.addColumn(Locations::getName)
                .setHeader("Название")
                .setAutoWidth(true);

        grid.addColumn(Locations::getPhoneNumber)
                .setHeader("Телефон")
                .setAutoWidth(true);

        grid.addColumn(Locations::getCountry)
                .setHeader("Страна")
                .setAutoWidth(true);

        grid.addColumn(Locations::getCity)
                .setHeader("Город")
                .setAutoWidth(true);

        grid.addColumn(Locations::getStreet)
                .setHeader("Улица")
                .setAutoWidth(true);

        grid.addColumn(Locations::getBuildingNumber)
                .setHeader("Номер строения")
                .setAutoWidth(true);

        grid.addColumn(Locations::getPostalCode)
                .setHeader("Почтовый индекс")
                .setAutoWidth(true);

        grid.addColumn(l -> {
                    LocationsType type = l.getLocationType();
                    return type != null ? type.getLocationTypeName() : "—";
                })
                .setHeader("Тип")
                .setAutoWidth(true);

        grid.addColumn(Locations::getEmployeeAmount)
                .setHeader("Количество сотрудников")
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

        binder.forField(phoneNumber)
                .asRequired("Обязательное поле")
                .bind(Locations::getPhoneNumber, Locations::setPhoneNumber);

        binder.forField(city)
                .asRequired("Обязательное поле")
                .bind(Locations::getCity, Locations::setCity);

        binder.forField(postalCode)
                .asRequired("Обязательное поле")
                .bind(Locations::getPostalCode, Locations::setPostalCode);

        binder.forField(country)
                .asRequired("Обязательное поле")
                .bind(Locations::getCountry, Locations::setCountry);

        binder.forField(buildingNumber)
                .bind(Locations::getBuildingNumber, Locations::setBuildingNumber);

        // Buttons layout
        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);
        buttons.setSpacing(true);

        form.add(name, phoneNumber, country, city, street, buildingNumber, postalCode, locationType, buttons);
        form.setVisible(false);

        // Event handlers
        saveButton.addClickListener(e -> saveLocation());
        saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        deleteButton.addClickListener(e -> deleteLocation());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        cancelButton.addClickListener(e -> cancelLocation());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        addButton.addClickListener(e -> addLocation());
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
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
    private void configureBackButton() {
        backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        backButton.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate(HRView.class))
        );
    }
}
