package com.example.application.views.components;

import com.example.application.data.components.CategoryOfComponent;
import com.example.application.data.components.Component;
import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.components.ComponentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

@PageTitle("Компоненты")
@Route("components")
@SpringComponent
@UIScope
//@Menu(order = 13, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed({"SALES","GOD"})
public class ComponentView extends HorizontalLayout {

    private final ComponentService service;

    private final Grid<Component> grid = new Grid<>(Component.class);
    private final ComboBox<TypeOfDevice> typeOfDeviceComboBox = new ComboBox<>("Тип устройства");
    private final ComboBox<CategoryOfComponent> categoryComboBox = new ComboBox<>("Категория компонента");
    private final TextField nameOfComponentField = new TextField("Название компонента");
    private final TextField costField = new TextField("Стоимость");
    private final Button saveButton = new Button("Сохранить");
    private final Button deleteButton = new Button("Удалить");

    @Autowired
    public ComponentView(ComponentService service) {
        this.service = service;

    //    configureGrid();
    //    configureForm();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(saveButton, deleteButton);

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("300px");
        formLayout.add(typeOfDeviceComboBox, categoryComboBox, nameOfComponentField, costField, buttonLayout);

        add(grid);
        add(formLayout);
    //    updateList();
    }
    /*
    private void configureGrid() {
        grid.removeAllColumns();

        grid.addColumn(Component::getComponentId).setHeader("ID компонента");
        grid.addColumn(component -> component.getCategory().getTypeOfPartName()).setHeader("Комплектующая");
        grid.addColumn(Component::getName).setHeader("Название");
        grid.addColumn(Component::getCost).setHeader("Стоимость");

        grid.asSingleSelect().addValueChangeListener(event -> editComponent(event.getValue()));
    }

    private void configureForm() {
        typeOfDeviceComboBox.setItems(service.findAllTypesOfDevices());
        typeOfDeviceComboBox.setItemLabelGenerator(TypeOfDevice::getTypeOfDeviceName);
        typeOfDeviceComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                categoryComboBox.setItems(service.findCategoriesByTypeOfDevice(event.getValue()));
            } else {
                categoryComboBox.clear();
            }
        });

        categoryComboBox.setItemLabelGenerator(CategoryOfComponent::getTypeOfPartName);

        saveButton.addClickListener(event -> saveComponent());
        deleteButton.addClickListener(event -> deleteComponent());
    }

    private void updateList() {
        grid.setItems(service.findAll());
    }

    private void editComponent(Component component) {
        if (component == null) {
            clearForm();
        } else {
            typeOfDeviceComboBox.setValue(component.getCategory().getTypeOfDevice());
            categoryComboBox.setValue(component.getCategory());
            nameOfComponentField.setValue(component.getName());
            costField.setValue(String.valueOf(component.getCost()));
        }
    }

    private void saveComponent() {
        Component component = new Component();
        component.setCategory(categoryComboBox.getValue());
        component.setName(nameOfComponentField.getValue());
        component.setCost(Double.parseDouble(costField.getValue()));
        service.save(component);
        updateList();
        clearForm();
    }

    private void deleteComponent() {
        Component component = grid.asSingleSelect().getValue();
        if (component != null) {
            service.delete(component);
            updateList();
            clearForm();
        }
    }

    private void clearForm() {
        typeOfDeviceComboBox.clear();
        categoryComboBox.clear();
        nameOfComponentField.clear();
        costField.clear();
        grid.asSingleSelect().clear();
    }

     */
}
