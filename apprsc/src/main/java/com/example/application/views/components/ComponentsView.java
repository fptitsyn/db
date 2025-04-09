package com.example.application.views.components;

import com.example.application.data.components.ComponentDTO;
import com.example.application.data.components.*;
import com.example.application.reports.employees.EmployeeInfoDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.DoubleToBigDecimalConverter;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    private final Button addButton = new Button("Комлектующая", VaadinIcon.PLUS_SQUARE_O.create());
    private final Button addCategoryButton = new Button("Категория", VaadinIcon.PLUS_SQUARE_O.create());
    private final Button addDeviceTypeButton = new Button("Тип устройства", VaadinIcon.PLUS_SQUARE_O.create());

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

        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        addCategoryButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addCategoryButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        addDeviceTypeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addDeviceTypeButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");


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

        // Колонка ID (без сортировки)
        grid.addColumn(ComponentDTO::getComponentId)
                .setHeader("ID")
                .setAutoWidth(true);

        // Колонка Device Type с сортировкой
        Grid.Column<ComponentDTO> deviceTypeCol = grid.addColumn(ComponentDTO::getTypeOfDeviceName)
                .setHeader("Device Type")
                .setKey("deviceType")
                .setSortable(true);

        // Колонка Part Type с сортировкой
        Grid.Column<ComponentDTO> partTypeCol = grid.addColumn(ComponentDTO::getTypeOfPartName)
                .setHeader("Part Type")
                .setKey("partType")
                .setSortable(true);

        // Колонка Component Name с сортировкой
        Grid.Column<ComponentDTO> componentNameCol = grid.addColumn(ComponentDTO::getComponentName)
                .setHeader("Component Name")
                .setKey("componentName")
                .setSortable(true);

        // Колонка Cost (без сортировки)
        grid.addColumn(
                        new NumberRenderer<>(
                                ComponentDTO::getCost,
                                NumberFormat.getCurrencyInstance(new Locale("ru", "RU"))
                        ))
                .setHeader("Cost");

        // Колонка с кнопкой удаления
        grid.addComponentColumn(item -> {
            Button deleteButton = new Button("", VaadinIcon.TRASH.create());
            deleteButton.addClickListener(e -> deleteComponent(item));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            deleteButton.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            return deleteButton;
        }).setWidth("100px").setFlexGrow(0);

        // Настройка сортировки по умолчанию
        List<GridSortOrder<ComponentDTO>> sortOrder = Arrays.asList(
                new GridSortOrder<>(deviceTypeCol, SortDirection.ASCENDING),
                new GridSortOrder<>(partTypeCol, SortDirection.ASCENDING),
                new GridSortOrder<>(componentNameCol, SortDirection.ASCENDING)
        );
        grid.sort(sortOrder);

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                componentEditor.editComponent(
                        componentService.getComponentById(e.getValue().getComponentId())
                );
            }
        });
    }

    // Метод для удаления компонента
    private void deleteComponent(ComponentDTO component) {
        try {
            componentService.deleteComponent(component.getComponentId());
            updateGrid();
            Notification.show("Component deleted", 3000, Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception ex) {
            Notification.show("Error deleting component: " + ex.getMessage(), 5000,
                            Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void configureEditors() {
        componentEditor.setWidth("500px");
        categoryEditor.setWidth("850px");
        deviceTypeEditor.setWidth("700px");

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
        private final Button saveButton = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());
        private final Button cancelButton = new Button("Отменить", VaadinIcon.CLOSE_CIRCLE_O.create());
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
            saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            saveButton.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            cancelButton.addClickListener(e -> close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cancelButton.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
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
        private final Button saveButton = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());
        private final Button cancelButton = new Button("Отменить", VaadinIcon.CLOSE_CIRCLE_O.create());
        private final Grid<CategoryDTO> grid = new Grid<>(CategoryDTO.class);
        private CategoryDTO currentDto;
        private final Binder<CategoryDTO> binder = new Binder<>(CategoryDTO.class);

        public CategoryEditor() {
            configureCombos();
            configureBinder();
            configureGrid();

            // Основной layout
            VerticalLayout formLayout = new VerticalLayout(
                    deviceTypeCombo,
                    nameField,
                    new HorizontalLayout(saveButton, cancelButton)
            );

            HorizontalLayout mainLayout = new HorizontalLayout(
                    formLayout,
                    grid
            );
            mainLayout.setWidthFull();
            mainLayout.setFlexGrow(1, grid);

            add(mainLayout);

            saveButton.addClickListener(e -> save());
            saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            saveButton.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            cancelButton.addClickListener(e -> close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cancelButton.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");

            refreshGrid(); // Загрузка данных при открытии
        }

        private void configureGrid() {
            grid.removeAllColumns();

            // Колонки
            grid.addColumn(CategoryDTO::getDeviceType)
                    .setHeader("Device Type")
                    .setAutoWidth(true);

            grid.addColumn(CategoryDTO::getName)
                    .setHeader("Category Name")
                    .setAutoWidth(true);

            // Колонка с кнопкой удаления
            grid.addComponentColumn(item -> {
                Button deleteButton = new Button("", VaadinIcon.TRASH.create());
                deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                deleteButton.getStyle()
                        .set("margin-right", "1em")
                        .set("color", "var(--lumo-primary-text-color)");
                deleteButton.addClickListener(e -> deleteCategory(item));
                return deleteButton;
            }).setWidth("70px").setFlexGrow(0);

            grid.setWidth("800px");
            grid.setHeight("300px");
        }

        private void deleteCategory(CategoryDTO dto) {
            try {
                categoryService.deleteCategory(dto.getId());
                refreshGrid();
                componentEditor.refreshDeviceTypes();
                Notification.show("Category deleted", 3000, Notification.Position.TOP_END);
            } catch (Exception ex) {
                Notification.show("Error deleting category: " + ex.getMessage(), 5000,
                                Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }

        private void refreshGrid() {
            grid.setItems(categoryService.getAllCategories());
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
                    refreshGrid(); // Обновляем таблицу после сохранения
                    componentEditor.refreshDeviceTypes();
                    updateGrid();
                    Notification.show("Category saved successfully", 3000,
                            Notification.Position.TOP_END);
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
        private final Button saveButton = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());
        private final Button cancelButton = new Button("Отменить", VaadinIcon.CLOSE_CIRCLE_O.create());
        private final Grid<TypeOfDeviceDTO> grid = new Grid<>(TypeOfDeviceDTO.class);
        private TypeOfDeviceDTO currentDto;
        private final Binder<TypeOfDeviceDTO> binder = new Binder<>(TypeOfDeviceDTO.class);

        public DeviceTypeEditor() {
            configureBinder();
            configureGrid();

            // Основной layout
            VerticalLayout formLayout = new VerticalLayout(
                    nameField,
                    new HorizontalLayout(saveButton, cancelButton)
            );

            HorizontalLayout mainLayout = new HorizontalLayout(
                    formLayout,
                    grid
            );
            mainLayout.setWidthFull();
            mainLayout.setFlexGrow(1, grid);

            add(mainLayout);

            saveButton.addClickListener(e -> save());
            saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            saveButton.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            cancelButton.addClickListener(e -> close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cancelButton.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");

            refreshGrid(); // Загрузка данных при открытии
        }

        private void configureGrid() {
            grid.removeAllColumns();

            // Колонка с названием
            grid.addColumn(TypeOfDeviceDTO::getName)
                    .setHeader("Existing Device Types")
                    .setAutoWidth(true);

            // Колонка с кнопкой удаления
            grid.addComponentColumn(item -> {
                Button deleteButton = new Button("", VaadinIcon.TRASH.create());
                deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
                deleteButton.getStyle()
                        .set("margin-right", "1em")
                        .set("color", "var(--lumo-primary-text-color)");
                deleteButton.addClickListener(e -> deleteDeviceType(item));
                return deleteButton;
            }).setWidth("70px").setFlexGrow(0);

            grid.setWidth("600px");
            grid.setHeight("200px");
        }
        private void deleteDeviceType(TypeOfDeviceDTO dto) {
            try {
                typeService.deleteDeviceType(dto.getId());
                refreshGrid();
                componentEditor.refreshDeviceTypes();
                categoryEditor.refreshDeviceTypes();
                Notification.show("Device type deleted", 3000, Notification.Position.TOP_END);
            } catch (Exception ex) {
                Notification.show("Error deleting device type: " + ex.getMessage(), 5000,
                                Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
        private void refreshGrid() {
            grid.setItems(typeService.getAllDeviceTypes());
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
                    refreshGrid(); // Обновляем таблицу после сохранения
                    componentEditor.refreshDeviceTypes();
                    categoryEditor.refreshDeviceTypes();
                    updateGrid();
                    Notification.show("Device type saved successfully", 3000,
                            Notification.Position.TOP_END);
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