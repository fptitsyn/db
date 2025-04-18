package com.example.application.views.services;

import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.services.Services;
import com.example.application.data.services.ServicesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Route("services")
@SpringComponent
@UIScope
@PageTitle("Услуги")
@Menu(order = 15, icon = LineAwesomeIconUrl.LAPTOP_CODE_SOLID)
@RolesAllowed({"SALES", "GOD"})
public class ServicesView extends VerticalLayout {
    private static final Logger logger = LoggerFactory.getLogger(ServicesView.class);
    private final ServicesService service;
    private final Grid<Services> grid = new Grid<>(Services.class);
    private final TextField serviceNameField = new TextField("Название услуги");
    private final BigDecimalField costField = new BigDecimalField("Стоимость");
    private final NumberField timeToCompleteField = new NumberField("Время выполнения (ч.)");
    private final ComboBox<TypeOfDevice> typeOfDeviceComboBox = new ComboBox<>("Тип устройства");
    private final Button saveButton = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());
    private final Button deleteButton = new Button("Удалить", VaadinIcon.CLOSE_CIRCLE_O.create());

    @Autowired
    public ServicesView(ServicesService service) {
        this.service = service;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        configureGrid();
        configureForm();

        add(grid, new HorizontalLayout(serviceNameField, costField, timeToCompleteField, typeOfDeviceComboBox),
                new HorizontalLayout(saveButton, deleteButton));

        updateList();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        // Сохраняем ссылки на колонки для сортировки
        Grid.Column<Services> typeColumn = grid.addColumn(s -> s.getTypeOfDevice().getTypeOfDeviceName())
                .setHeader("Тип устройства")
                .setSortable(true)
                .setComparator((s1, s2) ->
                        s1.getTypeOfDevice().getTypeOfDeviceName()
                                .compareToIgnoreCase(s2.getTypeOfDevice().getTypeOfDeviceName()));

        Grid.Column<Services> nameColumn = grid.addColumn(Services::getServiceName)
                .setHeader("Название").setSortable(true);

        // Остальные колонки без изменений
        grid.addColumn(
                new NumberRenderer<>(
                        Services::getCost,
                        NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
                ))
                        .setHeader("Стоимость").setTextAlign(ColumnTextAlign.END);

        grid.addColumn(s -> s.getTimeToCompleteHours() + " ч.")
                .setHeader("Время выполнения").setTextAlign(ColumnTextAlign.END);

        // Устанавливаем сортировку по умолчанию
        grid.setMultiSort(true);
        grid.sort(List.of(
                new GridSortOrder<>(typeColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(nameColumn, SortDirection.ASCENDING)
        ));

        grid.asSingleSelect().addValueChangeListener(event -> editService(event.getValue()));
    }

    private void configureForm() {
        typeOfDeviceComboBox.setItems(service.findAllTypesOfDevices());
        typeOfDeviceComboBox.setItemLabelGenerator(TypeOfDevice::getTypeOfDeviceName);

        saveButton.addClickListener(ignored -> saveService());
        saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        deleteButton.addClickListener(ignored -> deleteService());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
    }

    private void updateList() {
        grid.setItems(service.findAll());
    }

    private void editService(Services service) {
        if (service == null) {
            clearForm();
        } else {
            serviceNameField.setValue(service.getServiceName());
            costField.setValue(service.getCost());
            timeToCompleteField.setValue(service.getTimeToCompleteHours().doubleValue());
            typeOfDeviceComboBox.setValue(service.getTypeOfDevice());
        }
    }

    private void saveService() {
        try {
            Services serviceObj = new Services();
            serviceObj.setServiceName(serviceNameField.getValue());
            serviceObj.setCost(costField.getValue());
            serviceObj.setTimeToCompleteHours(timeToCompleteField.getValue().intValue());
            serviceObj.setTypeOfDevice(typeOfDeviceComboBox.getValue());

            service.save(serviceObj);
            updateList();
            clearForm();
        } catch (Exception e) {
            Notification.show("Ошибка сохранения: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            logger.error("Ошибка при сохранении услуги", e); // Replaced printStackTrace()
        }
    }

    private void deleteService() {
        Services serviceObj = grid.asSingleSelect().getValue();
        if (serviceObj != null) {
            service.delete(serviceObj);
            updateList();
            clearForm();
        }
    }

    private void clearForm() {
        serviceNameField.clear();
        costField.clear();
        timeToCompleteField.clear();
        typeOfDeviceComboBox.clear();
        grid.asSingleSelect().clear();
    }
}
