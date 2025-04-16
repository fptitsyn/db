package com.example.application.views.services;

import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.services.Services;
import com.example.application.data.services.ServicesService;
import com.example.application.views.employees.HRView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

@Route("employee-service-view")
@SpringComponent
@UIScope
@PageTitle("Подтверждение навыков")
@RolesAllowed({"HR", "GOD"})
public class EmployeeServiceForm extends VerticalLayout {
    private final ServicesService service;
    private final EmployeesService employeesService;
    private final Grid<Services> grid = new Grid<>(Services.class);
    private final Button backButton = new Button("Вернуться назад");
    private Employees selectedEmployee; // поле для хранения выбранного сотрудника

    @Autowired
    public EmployeeServiceForm(ServicesService service, EmployeesService employeesService) { // Изменено
        this.service = service;
        this.employeesService = employeesService;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        configureGrid();

        ComboBox<Employees> employeeComboBox = new ComboBox<>("Сотрудники");
        employeeComboBox.setItems(employeesService.findAll());
        employeeComboBox.setItemLabelGenerator(Employees::getFullName);
        employeeComboBox.setWidthFull();

        // Добавить слушатель выбора сотрудника
        employeeComboBox.addValueChangeListener(event -> {
            selectedEmployee = event.getValue();
            grid.getDataProvider().refreshAll(); // Обновить данные Grid
            updateList();
        });

        // Добавление элементов в layout
        configureBackButton();
        add(employeeComboBox, grid, backButton);
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
        grid.addComponentColumn(service -> {
            Checkbox checkbox = new Checkbox();
            boolean isAssigned = selectedEmployee != null &&
                    selectedEmployee.getServices().stream()
                            .anyMatch(s -> s.getServiceId().equals(service.getServiceId()));
            checkbox.setValue(isAssigned);
            checkbox.addValueChangeListener(e -> {
                if (selectedEmployee != null) {
                    Set<Services> services = selectedEmployee.getServices();
                    if (e.getValue()) {
                        services.add(service);
                    } else {
                        services.removeIf(s -> s.getServiceId().equals(service.getServiceId()));
                    }
                    employeesService.save(selectedEmployee);
                }
            });
            return checkbox;
        }).setHeader("Назначено");
    }

    private void updateList() {
        grid.setItems(service.findAll());
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
