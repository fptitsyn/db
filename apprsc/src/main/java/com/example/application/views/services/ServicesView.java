package com.example.application.views.services;

import com.example.application.data.Employees;
import com.example.application.data.Services;

import com.example.application.data.components.TypeOfDevice;
import com.example.application.services.ServicesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.stream.Collectors;

@Route("services")
@SpringComponent
@UIScope
@PageTitle("Услуги")
@Menu(order = 55, icon = LineAwesomeIconUrl.LAPTOP_CODE_SOLID)
@RolesAllowed({"SALES","GOD"})
public class ServicesView extends VerticalLayout {
    private final ServicesService service;

    private final Grid<Services> grid = new Grid<>(Services.class);
    private final TextField serviceNameField = new TextField("Название услуги");
    private final NumberField costField = new NumberField("Стоимость");
    private final NumberField timeToCompleteField = new NumberField("Время выполнения (мин.)");
    private final ComboBox<TypeOfDevice> typeOfDeviceComboBox = new ComboBox<>("Тип устройства");
    private final CheckboxGroup<Employees> employeesCheckboxGroup = new CheckboxGroup<>("Сотрудники");
    private final Button saveButton = new Button("Сохранить");
    private final Button deleteButton = new Button("Удалить");

    @Autowired
    public ServicesView(ServicesService service) {
        this.service = service;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        configureGrid();
        configureForm();

        add(grid, new HorizontalLayout(serviceNameField, costField, timeToCompleteField),
                typeOfDeviceComboBox, employeesCheckboxGroup,
                new HorizontalLayout(saveButton, deleteButton));

        updateList();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(Services::getServiceId).setHeader("ID");
        grid.addColumn(Services::getServiceName).setHeader("Название");
        grid.addColumn(s -> s.getTypeOfDevice().getTypeOfDeviceName()).setHeader("Тип устройства");
        grid.addColumn(Services::getCost).setHeader("Стоимость");
        grid.addColumn(s -> s.getTimeToCompleteMinutes() + " мин.").setHeader("Время выполнения");
        grid.addColumn(s -> s.getEmployees().stream()
                        .map(Employees::getFullName)
                        .collect(Collectors.joining(", ")))
                .setHeader("Сотрудники");

        grid.asSingleSelect().addValueChangeListener(event -> editService(event.getValue()));
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%d ч. %d мин.", hours, minutes);
    }

    private void configureForm() {
        typeOfDeviceComboBox.setItems(service.findAllTypesOfDevices());
        typeOfDeviceComboBox.setItemLabelGenerator(TypeOfDevice::getTypeOfDeviceName);

        employeesCheckboxGroup.setItems(service.findAllEmployees());
        employeesCheckboxGroup.setItemLabelGenerator(Employees::getFullName);

        saveButton.addClickListener(event -> saveService());
        deleteButton.addClickListener(event -> deleteService());
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
            timeToCompleteField.setValue(service.getTimeToCompleteMinutes().doubleValue());
            typeOfDeviceComboBox.setValue(service.getTypeOfDevice());
            employeesCheckboxGroup.setValue(service.getEmployees());
        }
    }

    private void saveService() {
        try {
            Services serviceObj = new Services();
            serviceObj.setServiceName(serviceNameField.getValue());
            serviceObj.setCost(costField.getValue());
            serviceObj.setTimeToCompleteMinutes(timeToCompleteField.getValue().intValue());
            serviceObj.setTypeOfDevice(typeOfDeviceComboBox.getValue());
            serviceObj.setEmployees(new HashSet<>(employeesCheckboxGroup.getSelectedItems()));

            service.save(serviceObj);
            updateList();
            clearForm();
        } catch (Exception e) {
            Notification.show("Ошибка сохранения: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            e.printStackTrace();
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
        employeesCheckboxGroup.deselectAll();
        grid.asSingleSelect().clear();
    }
}
