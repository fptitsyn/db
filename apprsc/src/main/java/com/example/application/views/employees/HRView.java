package com.example.application.views.employees;

import com.example.application.data.services.Services;
import com.example.application.reports.employees.EmployeeInfoDTO;
import com.example.application.reports.employees.EmployeeInfoService;
import com.example.application.views.locations.LocationsForm;
import com.example.application.views.services.EmployeeServiceForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@PageTitle("Персонал")
@Route("HRView")
@Menu(order = 40, icon = LineAwesomeIconUrl.USER_FRIENDS_SOLID)
@RolesAllowed({"HR", "GOD"})
public class HRView extends VerticalLayout {
    private final transient EmployeeInfoService service;
    private final Grid<EmployeeInfoDTO> grid = new Grid<>(EmployeeInfoDTO.class, false);
    private final Button button1 = new Button("Сотрудники", VaadinIcon.USERS.create(), ignored -> showEmployeesForm());
    private final Button button2 = new Button("Приём/Увольнение", VaadinIcon.REFRESH.create(), ignored -> showEmployeesMovingForm());
    private final Button button3 = new Button("Штатное расписание", VaadinIcon.FILE_CODE.create(), ignored -> showStaffingTableForm());
    private final Button button4 = new Button("Офисы", VaadinIcon.WORKPLACE.create(), ignored -> showLocationsForm());
    private final Button button5 = new Button("Привязать услуги", VaadinIcon.LAPTOP.create(), ignored -> showEmployeeServiceForm());

    public HRView(EmployeeInfoService service) {
        this.service = service;
        configureGrid();
        configureBackButton();

        HorizontalLayout buttonLayout = new HorizontalLayout(button1, button5, button2, button3, button4);
        buttonLayout.setWidthFull();

        // Основной контейнер
        VerticalLayout mainLayout = new VerticalLayout(buttonLayout, grid);
        mainLayout.setSizeFull();
        mainLayout.setFlexGrow(1, grid); // Grid будет занимать все доступное пространство
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);

        add(mainLayout);
        setSizeFull();
        refreshGrid();
    }

    private void configureGrid() {
        Grid.Column<EmployeeInfoDTO> locationColumn = grid.addColumn(EmployeeInfoDTO::workplace).setHeader("Офис");
        Grid.Column<EmployeeInfoDTO> departmentColumn = grid.addColumn(EmployeeInfoDTO::department).setHeader("Подразделение");
        Grid.Column<EmployeeInfoDTO> positionColumn = grid.addColumn(EmployeeInfoDTO::position).setHeader("Должность");
        grid.addColumn(
                        new NumberRenderer<>(
                                EmployeeInfoDTO::salary,
                                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
                        ))
                .setHeader("Зарплата").setTextAlign(ColumnTextAlign.END);

        grid.addColumn(EmployeeInfoDTO::lastName).setHeader("Фамилия");
        grid.addColumn(EmployeeInfoDTO::firstName).setHeader("Имя");
        grid.addColumn(EmployeeInfoDTO::middleName).setHeader("Отчество");
        grid.addColumn(EmployeeInfoDTO::age).setHeader("Возраст");

        // Устанавливаем сортировку по умолчанию
        grid.setMultiSort(true);
        grid.sort(List.of(
                new GridSortOrder<>(locationColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(departmentColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(positionColumn, SortDirection.ASCENDING)
        ));

        grid.setSizeFull();
        grid.getStyle().set("flex-grow", "1");
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

    private void showEmployeeServiceForm() {
        getUI().ifPresent(ui -> ui.navigate(EmployeeServiceForm.class));
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
        button5.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button5.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
    }
}
