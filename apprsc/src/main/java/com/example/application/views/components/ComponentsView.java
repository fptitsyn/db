package com.example.application.views.components;

import com.example.application.data.components.ComponentDTO;
import com.example.application.data.components.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.DoubleToBigDecimalConverter;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.math.BigDecimal;

@Route("components-management")
@Menu(order = 16, icon = LineAwesomeIconUrl.MICROCHIP_SOLID)
@RolesAllowed({"SALES","GOD"})
@PageTitle("Комлектующие")
@SpringComponent
public class ComponentsView extends VerticalLayout {

    private final ComponentService componentService;
    private final TypeOfDeviceService typeService;
    private final CategoryOfComponentService categoryService;

    private final Grid<ComponentDTO> grid = new Grid<>(ComponentDTO.class);
    private final Button addButton = new Button("Add Component");
    private final Button addCategoryButton = new Button("Add Category");
    private final Button addDeviceTypeButton = new Button("Add Device Type");

    private final ComponentEditor componentEditor;
    private final CategoryEditor categoryEditor;
    private final DeviceTypeEditor deviceTypeEditor;

    @Autowired
    public ComponentsView(ComponentService componentService,
                          TypeOfDeviceService typeService,
                          CategoryOfComponentService categoryService) {
        this.componentService = componentService;
        this.typeService = typeService;
        this.categoryService = categoryService;

        this.componentEditor = new ComponentEditor();
        this.categoryEditor = new CategoryEditor();
        this.deviceTypeEditor = new DeviceTypeEditor();

        configureUI();
        updateGrid();
    }

    private void configureUI() {
        setSizeFull();
        configureGrid();
        configureEditors();

        HorizontalLayout buttonLayout = new HorizontalLayout(
                addButton,
                addCategoryButton,
                addDeviceTypeButton
        );
        buttonLayout.setSpacing(true);

        add(buttonLayout, grid, componentEditor, categoryEditor, deviceTypeEditor);
    }

    private void configureGrid() {
        grid.setHeight("70vh");
        grid.removeAllColumns();

        grid.addColumn(ComponentDTO::getComponentId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(ComponentDTO::getTypeOfDeviceName).setHeader("Device Type");
        grid.addColumn(ComponentDTO::getTypeOfPartName).setHeader("Part Type");
        grid.addColumn(ComponentDTO::getComponentName).setHeader("Component Name");
        grid.addColumn(c -> String.format("$%.2f", c.getCost())).setHeader("Cost");

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                componentEditor.editComponent(
                        componentService.getComponentById(e.getValue().getComponentId())
                );
            }
        });
    }

    private void configureEditors() {
        componentEditor.setWidth("500px");
        categoryEditor.setWidth("400px");
        deviceTypeEditor.setWidth("400px");

        addButton.addClickListener(e -> componentEditor.editComponent(new ComponentDTO()));
        addCategoryButton.addClickListener(e -> categoryEditor.editCategory(new CategoryDTO()));
        addDeviceTypeButton.addClickListener(e -> deviceTypeEditor.editDeviceType(new TypeOfDeviceDTO()));
    }

    private void updateGrid() {
        grid.setItems(componentService.findAllComponents());
    }

    private class ComponentEditor extends Dialog {
        public final ComboBox<String> deviceTypeCombo = new ComboBox<>("Device Type");
        public final ComboBox<String> categoryCombo = new ComboBox<>("Category");
        private final TextField nameField = new TextField("Component Name");
        private final NumberField costField = new NumberField("Cost");
        private final Button saveButton = new Button("Save");
        private final Button cancelButton = new Button("Cancel");
        private ComponentDTO currentDto;
        private final Binder<ComponentDTO> binder = new Binder<>(ComponentDTO.class);

        public ComponentEditor() {
            configureCombos();
            configureBinder();

            VerticalLayout layout = new VerticalLayout(
                    deviceTypeCombo,
                    categoryCombo,
                    nameField,
                    costField,
                    new HorizontalLayout(saveButton, cancelButton)
            );
            add(layout);

            saveButton.addClickListener(e -> save());
            cancelButton.addClickListener(e -> close());
        }

        private void configureCombos() {
            refreshDeviceTypes();
            deviceTypeCombo.setAllowCustomValue(true);
            deviceTypeCombo.addCustomValueSetListener(e ->
                    deviceTypeCombo.setValue(e.getDetail()));

            deviceTypeCombo.addValueChangeListener(e -> {
                if (e.getValue() != null) {
                    categoryCombo.setItems(
                            categoryService.getPartNamesByDeviceType(e.getValue())
                    );
                }
            });

            categoryCombo.setAllowCustomValue(true);
            categoryCombo.addCustomValueSetListener(e ->
                    categoryCombo.setValue(e.getDetail()));
        }

        public void refreshDeviceTypes() {
            deviceTypeCombo.setItems(typeService.getAllDeviceTypeNames());
        }

        private void configureBinder() {
            binder.forField(nameField)
                    .asRequired("Name is required")
                    .bind(ComponentDTO::getComponentName, ComponentDTO::setComponentName);

            binder.forField(costField)
                    .withConverter(new DoubleToBigDecimalConverter())
                    .withValidator(cost -> cost.compareTo(BigDecimal.ZERO) >= 0, "Cost cannot be negative")
                    .asRequired("Cost is required")
                    .bind(ComponentDTO::getCost, ComponentDTO::setCost);

            binder.forField(deviceTypeCombo)
                    .asRequired("Device type is required")
                    .bind(ComponentDTO::getTypeOfDeviceName, ComponentDTO::setTypeOfDeviceName);

            binder.forField(categoryCombo)
                    .asRequired("Category is required")
                    .bind(ComponentDTO::getTypeOfPartName, ComponentDTO::setTypeOfPartName);
        }

        public void editComponent(ComponentDTO dto) {
            currentDto = dto != null ? dto : new ComponentDTO();

            if (currentDto.getCost() == null) currentDto.setCost(BigDecimal.ZERO);
            if (currentDto.getTypeOfDeviceName() != null) {
                categoryCombo.setItems(
                        categoryService.getPartNamesByDeviceType(currentDto.getTypeOfDeviceName())
                );
            }

            binder.readBean(currentDto);
            open();
        }

        private void save() {
            try {
                if (binder.writeBeanIfValid(currentDto)) {
                    componentService.saveComponent(currentDto);
                    updateGrid();
                    close();
                }
            } catch (Exception e) {
                Notification.show("Error saving component: " + e.getMessage(), 5000,
                                Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    private class CategoryEditor extends Dialog {
        public final ComboBox<String> deviceTypeCombo = new ComboBox<>("Device Type");
        private final TextField nameField = new TextField("Category Name");
        private final Button saveButton = new Button("Save");
        private final Button cancelButton = new Button("Cancel");
        private CategoryDTO currentDto;
        private final Binder<CategoryDTO> binder = new Binder<>(CategoryDTO.class);

        public CategoryEditor() {
            configureCombos();
            configureBinder();

            VerticalLayout layout = new VerticalLayout(
                    deviceTypeCombo,
                    nameField,
                    new HorizontalLayout(saveButton, cancelButton)
            );
            add(layout);

            saveButton.addClickListener(e -> save());
            cancelButton.addClickListener(e -> close());
        }

        private void configureCombos() {
            refreshDeviceTypes();
            deviceTypeCombo.setAllowCustomValue(true);
        }

        public void refreshDeviceTypes() {
            deviceTypeCombo.setItems(typeService.getAllDeviceTypeNames());
        }

        private void configureBinder() {
            binder.forField(nameField)
                    .asRequired("Name is required")
                    .bind(CategoryDTO::getName, CategoryDTO::setName);

            binder.forField(deviceTypeCombo)
                    .asRequired("Device Type is required")
                    .bind(CategoryDTO::getDeviceType, CategoryDTO::setDeviceType);
        }

        public void editCategory(CategoryDTO dto) {
            currentDto = dto != null ? dto : new CategoryDTO();
            binder.readBean(currentDto);
            open();
        }

        private void save() {
            try {
                if (binder.writeBeanIfValid(currentDto)) {
                    categoryService.saveCategory(currentDto);
                    updateGrid();
                    close();
                }
            } catch (Exception e) {
                Notification.show("Error saving category: " + e.getMessage(), 5000,
                                Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    private class DeviceTypeEditor extends Dialog {
        private final TextField nameField = new TextField("Device Type Name");
        private final Button saveButton = new Button("Save");
        private final Button cancelButton = new Button("Cancel");
        private TypeOfDeviceDTO currentDto;
        private final Binder<TypeOfDeviceDTO> binder = new Binder<>(TypeOfDeviceDTO.class);

        public DeviceTypeEditor() {
            configureBinder();

            VerticalLayout layout = new VerticalLayout(
                    nameField,
                    new HorizontalLayout(saveButton, cancelButton)
            );
            add(layout);

            saveButton.addClickListener(e -> save());
            cancelButton.addClickListener(e -> close());
        }

        private void configureBinder() {
            binder.forField(nameField)
                    .asRequired("Name is required")
                    .bind(TypeOfDeviceDTO::getName, TypeOfDeviceDTO::setName);
        }

        public void editDeviceType(TypeOfDeviceDTO dto) {
            currentDto = dto != null ? dto : new TypeOfDeviceDTO();
            binder.readBean(currentDto);
            open();
        }

        private void save() {
            try {
                if (binder.writeBeanIfValid(currentDto)) {
                    typeService.saveDeviceType(currentDto);
                    componentEditor.refreshDeviceTypes();
                    categoryEditor.refreshDeviceTypes();
                    updateGrid();
                    close();
                }
            } catch (Exception e) {
                Notification.show("Error saving device type: " + e.getMessage(), 5000,
                                Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }
}