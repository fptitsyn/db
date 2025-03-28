package com.example.application.views.components;

import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.components.TypeOfDeviceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Типы устройства компонентов")
@Route("types-of-devices")
@SpringComponent
@UIScope
//@Menu(order = 15, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed({"SALES","GOD"})
public class TypeOfDeviceView extends HorizontalLayout {

    private final TypeOfDeviceService service;

    private final Grid<TypeOfDevice> grid = new Grid<>(TypeOfDevice.class);
    private final TextField typeNameField = new TextField("Название типа");
    private final Button saveButton = new Button("Сохранить");
    private final Button deleteButton = new Button("Удалить");

    @Autowired
    public TypeOfDeviceView(TypeOfDeviceService service) {
        this.service = service;

        //configureGrid();
        //configureForm();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(saveButton, deleteButton);

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("300px");
        formLayout.add(typeNameField, buttonLayout);

        add(grid);
        add(formLayout);
        //updateList();

    }
    /*
    private void configureGrid() {
        // Удаляем все автоматически сгенерированные столбцы
        grid.removeAllColumns();

        // Добавляем столбец для typeOfDeviceId с кастомным заголовком
        grid.addColumn(TypeOfDevice::getTypeOfDeviceId)
                .setHeader("ID типа устройства");

        // Добавляем столбец для typeName с кастомным заголовком
        grid.addColumn(TypeOfDevice::getTypeOfDeviceName)
                .setHeader("Название типа устройства");

        // Настройка слушателя для редактирования
        grid.asSingleSelect().addValueChangeListener(event -> editTypeOfDevice(event.getValue()));
    }

    private void configureForm() {
        saveButton.addClickListener(event -> saveTypeOfDevice());
        deleteButton.addClickListener(event -> deleteTypeOfDevice());
    }

    private void updateList() {
        grid.setItems(service.findAll());
    }

    private void editTypeOfDevice(TypeOfDevice typeOfDevice) {
        if (typeOfDevice == null) {
            clearForm();
        } else {
            typeNameField.setValue(typeOfDevice.getTypeOfDeviceName());
        }
    }

    private void saveTypeOfDevice() {
        TypeOfDevice typeOfDevice = new TypeOfDevice();
        typeOfDevice.setTypeOfDeviceName(typeNameField.getValue());
        service.save(typeOfDevice);
        updateList();
        clearForm();
    }

    private void deleteTypeOfDevice() {
        TypeOfDevice typeOfDevice = grid.asSingleSelect().getValue();
        if (typeOfDevice != null) {
            service.delete(typeOfDevice);
            updateList();
            clearForm();
        }
    }

    private void clearForm() {
        typeNameField.clear();
        grid.asSingleSelect().clear();
    }

     */
}