package com.example.application.views.reports;

import com.example.application.reports.employees.EmployeeInfoDTO;
import com.example.application.reports.employees.EmployeeInfoService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@PageTitle("Отчет по сотрудникам")
@Route("employees-info")
@Menu(order = 45, icon = LineAwesomeIconUrl.CHART_BAR_SOLID)
@RolesAllowed({"HR", "ANALYSTS", "GOD"})

public class EmployeeInfoView extends VerticalLayout {
    private final transient EmployeeInfoService service;
    private final Grid<EmployeeInfoDTO> grid = new Grid<>(EmployeeInfoDTO.class, false);

    public EmployeeInfoView(EmployeeInfoService service) {
        this.service = service;

        initForm();
    }

    private void initForm() {
        Button exportBtn = new Button("Открыть в Excel", VaadinIcon.TABLE.create(), ignored -> exportToExcel());
        styleButton(exportBtn, "primary");
        configureGrid();
        setSizeFull();
        add(grid,
                new HorizontalLayout(exportBtn) {{
                    setWidthFull();
                    setJustifyContentMode(JustifyContentMode.END);
                }});
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(EmployeeInfoDTO::lastName).setHeader("Фамилия");
        grid.addColumn(EmployeeInfoDTO::firstName).setHeader("Имя");
        grid.addColumn(EmployeeInfoDTO::middleName).setHeader("Отчество");
        grid.addColumn(EmployeeInfoDTO::dateOfBirth).setHeader("Дата рождения");
        grid.addColumn(EmployeeInfoDTO::phoneNumber).setHeader("Телефон");
        grid.addColumn(EmployeeInfoDTO::email).setHeader("e-mail");
        grid.addColumn(EmployeeInfoDTO::age).setHeader("Возраст");
        grid.addColumn(EmployeeInfoDTO::department).setHeader("Подразделение");
        grid.addColumn(EmployeeInfoDTO::position).setHeader("Должность");

        grid.addColumn(
                        new NumberRenderer<>(
                                EmployeeInfoDTO::salary,
                                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
                        ))
                .setHeader("Зарплата").setTextAlign(ColumnTextAlign.END);

        grid.addColumn(EmployeeInfoDTO::workplace).setHeader("Офис");
        grid.addColumn(EmployeeInfoDTO::experience).setHeader("Стаж (лет)");
    }

    private void styleButton(Button button, String theme) {
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        if ("primary".equals(theme)) {
            button.getStyle().set("color", "var(--lumo-primary-text-color)");
        } else {
            button.getStyle().set("color", "var(--lumo-error-text-color)");
        }
        button.getStyle().set("margin-right", "1em");
    }

    private void exportToExcel() {
        try {
            StreamResource resource = new StreamResource("employees.xlsx", () -> {
                // Создаем Workbook внутри лямбды
                try (Workbook workbook = new XSSFWorkbook();
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                    Sheet sheet = workbook.createSheet("Employees");
                    createHeaderRow(sheet);
                    fillDataRows(sheet, workbook);
                    autoSizeColumns(sheet);

                    workbook.write(out);
                    return new ByteArrayInputStream(out.toByteArray());

                } catch (IOException e) {
                    throw new RuntimeException("Excel creation failed", e);
                }
            });

            // Регистрация ресурса и скачивание
            StreamRegistration registration = UI.getCurrent().getSession()
                    .getResourceRegistry()
                    .registerResource(resource);

            UI.getCurrent().getPage().executeJs(
                    "const link = document.createElement('a');" +
                            "link.href = $0;" +
                            "link.download = $1;" +
                            "document.body.appendChild(link);" +
                            "link.click();" +
                            "document.body.removeChild(link);",
                    registration.getResourceUri().toString(),
                    "employees.xlsx"
            );

        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }


    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Фамилия", "Имя", "Отчество", "Дата рождения", "Телефон",
                "e-mail", "Возраст", "Подразделение", "Должность", "Зарплата", "Офис", "Стаж"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    private void fillDataRows(Sheet sheet, Workbook workbook) {
        // Используем переданный workbook
        List<EmployeeInfoDTO> employees = service.getAllEmployeeInfo();
        int rowNum = 1;

        for (EmployeeInfoDTO employee : employees) {
            Row row = sheet.createRow(rowNum++);
            // ... заполнение данных
            row.createCell(0).setCellValue(employee.lastName());
            row.createCell(1).setCellValue(employee.firstName());
            row.createCell(2).setCellValue(employee.middleName());
            row.createCell(3).setCellValue(employee.dateOfBirth().toString());
            row.createCell(4).setCellValue(employee.phoneNumber());
            row.createCell(5).setCellValue(employee.email());
            row.createCell(6).setCellValue(employee.age());
            row.createCell(7).setCellValue(employee.department());
            row.createCell(8).setCellValue(employee.position());
            row.createCell(9).setCellValue(employee.salary().doubleValue());
            row.createCell(10).setCellValue(employee.workplace());
            row.createCell(11).setCellValue(employee.experience());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 12; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private ByteArrayInputStream createInputStream(Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Excel file", e);
        }
    }

    private void refreshGrid() {
        grid.setItems(service.getAllEmployeeInfo());
    }

    private static class FileDownloadHelper {
        public static void triggerDownload(StreamResource resource) {
            UI.getCurrent().getPage().executeJs(
                    "const link = document.createElement('a');" +
                            "link.href = $0;" +
                            "link.download = $1;" +
                            "document.body.appendChild(link);" +
                            "link.click();" +
                            "document.body.removeChild(link);",
                    resource.getName()
            );
        }
    }
}