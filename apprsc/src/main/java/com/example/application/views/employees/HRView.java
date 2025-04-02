package com.example.application.views.employees;

import com.example.application.reports.employees.EmployeeInfoDTO;
import com.example.application.reports.employees.EmployeeInfoService;
import com.example.application.views.locations.LocationsForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Персонал")
@Route("HRView")

@Menu(order = 40, icon = LineAwesomeIconUrl.USER_FRIENDS_SOLID)
@RolesAllowed({"HR","GOD"})
public class HRView extends VerticalLayout {
    private final transient EmployeeInfoService service;
    private final Grid<EmployeeInfoDTO> grid = new Grid<>(EmployeeInfoDTO.class, false);
    private Button button1 = new Button("Сотрудники", VaadinIcon.USERS.create(), event -> showEmployeesForm());
    private Button button2 = new Button("Приём/Увольнение", VaadinIcon.REFRESH.create(), event -> showEmployeesMovingForm());
    private Button button3 = new Button("Штатное расписание", VaadinIcon.FILE_CODE.create(), event -> showStaffingTableForm());
    private Button button4 = new Button("Офисы", VaadinIcon.WORKPLACE.create(), event -> showLocationsForm());

    public HRView(EmployeeInfoService service) {
        this.service = service;
        configureGrid();
        configureBackButton();
        HorizontalLayout buttonLayout = new HorizontalLayout(button1, button2, button3, button4);
        add(buttonLayout, grid);
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(EmployeeInfoDTO::workplace).setHeader("Офис");
        grid.addColumn(EmployeeInfoDTO::department).setHeader("Подразделение");
        grid.addColumn(EmployeeInfoDTO::position).setHeader("Должность");
        grid.addColumn(EmployeeInfoDTO::salary).setHeader("Зарплата");
        grid.addColumn(EmployeeInfoDTO::lastName).setHeader("Фамилия");
        grid.addColumn(EmployeeInfoDTO::firstName).setHeader("Имя");
        grid.addColumn(EmployeeInfoDTO::middleName).setHeader("Отчество");
        grid.addColumn(EmployeeInfoDTO::age).setHeader("Возраст");
    }

    private void refreshGrid() {
        grid.setItems(service.getAllEmployeeInfo());
    }

    private void showEmployeesForm() {
        getUI().ifPresent(ui -> ui.navigate(EmployeesForm.class));
    }
    private void showEmployeesMovingForm() {
        getUI().ifPresent(ui -> ui.navigate(EmployeesMovingForm.class));
    }
    private void showStaffingTableForm() {
        getUI().ifPresent(ui -> ui.navigate(StaffingTableForm.class));
    }
    private void showLocationsForm() {
        getUI().ifPresent(ui -> ui.navigate(LocationsForm.class));
    }
    private void configureBackButton() {
        button1.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button1.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        button2.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button2.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        button3.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button3.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        button4.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button4.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
    }
}
