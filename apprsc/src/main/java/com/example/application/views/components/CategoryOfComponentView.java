package com.example.application.views.components;

import com.example.application.data.components.CategoryOfComponent;
import com.example.application.data.components.TypeOfDevice;
import com.example.application.data.components.CategoryOfComponentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Комплектующие компонента")
@Route("categories-of-components")
//@SpringComponent
//@UIScope
//@Menu(order = 14, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed({"SALES","GOD"})
public class CategoryOfComponentView extends HorizontalLayout {

    private final CategoryOfComponentService service;

    private final Grid<CategoryOfComponent> grid = new Grid<>(CategoryOfComponent.class);
    private final ComboBox<TypeOfDevice> typeOfDeviceComboBox = new ComboBox<>("Тип устройства");
    private final TextField typeNameField = new TextField("Название комплектующей");
    private final Button saveButton = new Button("Сохранить");
    private final Button deleteButton = new Button("Удалить");

    @Autowired
    public CategoryOfComponentView(CategoryOfComponentService service) {
        this.service = service;

        configureGrid();
        configureForm();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(saveButton, deleteButton);

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("300px");
        formLayout.add(typeOfDeviceComboBox, typeNameField, buttonLayout);

        add(grid);
        add(formLayout);
        updateList();
    }

    private void configureGrid() {
        // Удаляем автоматически сгенерированные столбцы
        grid.removeAllColumns();

        // Добавляем столбцы с кастомными заголовками
        grid.addColumn(CategoryOfComponent::getCategoryOfComponentId)
                .setHeader("ID категории");

        grid.addColumn(category -> category.getTypeOfDevice().getTypeOfDeviceName())
                .setHeader("Тип устройства");

        grid.addColumn(CategoryOfComponent::getTypeOfPartName)
                .setHeader("Название комплектующей");

        // Настройка слушателя для редактирования
        grid.asSingleSelect().addValueChangeListener(event -> editCategoryOfComponent(event.getValue()));
    }

    private void configureForm() {
        typeOfDeviceComboBox.setItems(query ->
                service.findAllTypesOfDevices().stream()
        );
        typeOfDeviceComboBox.setItems(service.findAllTypesOfDevices());
        typeOfDeviceComboBox.setItemLabelGenerator(TypeOfDevice::getTypeOfDeviceName);

        saveButton.addClickListener(event -> saveCategoryOfComponent());
        deleteButton.addClickListener(event -> deleteCategoryOfComponent());
    }

    private void updateList() {
        grid.setItems(service.findAll());
    }

    private void editCategoryOfComponent(CategoryOfComponent categoryOfComponent) {
        if (categoryOfComponent == null) {
            clearForm();
        } else {
            typeOfDeviceComboBox.setValue(categoryOfComponent.getTypeOfDevice());
            typeNameField.setValue(categoryOfComponent.getTypeOfPartName());
        }
    }

    private void saveCategoryOfComponent() {
        CategoryOfComponent categoryOfComponent = new CategoryOfComponent();
        categoryOfComponent.setTypeOfDevice(typeOfDeviceComboBox.getValue());
        categoryOfComponent.setTypeOfPartName(typeNameField.getValue());
        service.save(categoryOfComponent);
        updateList();
        clearForm();
    }

    private void deleteCategoryOfComponent() {
        CategoryOfComponent categoryOfComponent = grid.asSingleSelect().getValue();
        if (categoryOfComponent != null) {
            service.delete(categoryOfComponent);
            updateList();
            clearForm();
        }
    }

    private void clearForm() {
        typeOfDeviceComboBox.clear();
        typeNameField.clear();
        grid.asSingleSelect().clear();
    }


}
