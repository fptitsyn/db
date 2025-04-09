package com.example.application.views.employees;

import com.example.application.data.employees.StaffingTable;
import com.example.application.reports.employees.EmployeeInfoDTO;
import com.example.application.reports.employees.EmployeeInfoService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@PageTitle("Отчет по сотрудникам")
@Route("employees-info")
@Menu(order = 45, icon = LineAwesomeIconUrl.CHART_BAR_SOLID)
@RolesAllowed({"HR","ANALYSTS", "GOD"})

public class EmployeeInfoView extends VerticalLayout {
    private final transient EmployeeInfoService service;
    private final Grid<EmployeeInfoDTO> grid = new Grid<>(EmployeeInfoDTO.class, false);

    public EmployeeInfoView(EmployeeInfoService service) {
        this.service = service;
        configureGrid();
        add(grid, createExportButton());
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(EmployeeInfoDTO::lastName).setHeader("Last Name");
        grid.addColumn(EmployeeInfoDTO::firstName).setHeader("First Name");
        grid.addColumn(EmployeeInfoDTO::middleName).setHeader("Middle Name");
        grid.addColumn(EmployeeInfoDTO::dateOfBirth).setHeader("Birth Date");
        grid.addColumn(EmployeeInfoDTO::phoneNumber).setHeader("Phone");
        grid.addColumn(EmployeeInfoDTO::email).setHeader("Email");
        grid.addColumn(EmployeeInfoDTO::age).setHeader("Age");
        grid.addColumn(EmployeeInfoDTO::department).setHeader("Department");
        grid.addColumn(EmployeeInfoDTO::position).setHeader("Position");

        grid.addColumn(
                new NumberRenderer<>(
                        EmployeeInfoDTO::salary,
                        NumberFormat.getCurrencyInstance(new Locale("ru", "RU"))
                ))
                .setHeader("Salary");

        grid.addColumn(EmployeeInfoDTO::workplace).setHeader("Workplace");
        grid.addColumn(EmployeeInfoDTO::experience).setHeader("Experience (years)");
    }

    private Button createExportButton() {
        return new Button("Открыть в Excel", VaadinIcon.BAR_CHART.create(), event -> exportToExcel());
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
                "Last Name", "First Name", "Middle Name", "Birth Date", "Phone",
                "Email", "Age", "Department", "Position", "Salary", "Workplace", "Experience"
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