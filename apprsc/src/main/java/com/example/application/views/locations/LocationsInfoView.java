package com.example.application.views.locations;

import com.example.application.reports.employees.EmployeeInfoDTO;
import com.example.application.reports.employees.EmployeeInfoService;
import com.example.application.reports.locations.LocationsInfoDTO;
import com.example.application.reports.locations.LocationsInfoService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import java.util.List;

@PageTitle("Отчет по офисам")
@Route("locations-info")
@Menu(order = 49, icon = LineAwesomeIconUrl.CHART_BAR_SOLID)
@RolesAllowed({"HR","ANALYSTS", "GOD"})

public class LocationsInfoView extends VerticalLayout {
    private final transient LocationsInfoService service;
    private final Grid<LocationsInfoDTO> grid = new Grid<>(LocationsInfoDTO.class, false);

    public LocationsInfoView(LocationsInfoService service) {
        this.service = service;
        configureGrid();
        add(grid, createExportButton());
        refreshGrid();
    }

    private void configureGrid() {
        grid.addColumn(LocationsInfoDTO::location_name).setHeader("Название");
        grid.addColumn(LocationsInfoDTO::phone_number).setHeader("Телефон");
        grid.addColumn(LocationsInfoDTO::country).setHeader("Страна");
        grid.addColumn(LocationsInfoDTO::city).setHeader("Город");
        grid.addColumn(LocationsInfoDTO::street).setHeader("Улица");
        grid.addColumn(LocationsInfoDTO::building_number).setHeader("Номер строения");
        grid.addColumn(LocationsInfoDTO::postal_code).setHeader("Почтовый индекс");
        grid.addColumn(LocationsInfoDTO::location_type_name).setHeader("Специализация офиса");
    }

    private Button createExportButton() {
        return new Button("Открыть в Excel", VaadinIcon.BAR_CHART.create(), event -> exportToExcel());
    }

    private void exportToExcel() {
        try {
            StreamResource resource = new StreamResource("locations.xlsx", () -> {
                // Создаем Workbook внутри лямбды
                try (Workbook workbook = new XSSFWorkbook();
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                    Sheet sheet = workbook.createSheet("Locations");
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
                    "locations.xlsx"
            );

        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }


    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Название", "Телефон", "Страна", "Город", "Улица",
                "Номер строения", "Почтовый индекс", "Специализация офиса"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }
    private void fillDataRows(Sheet sheet, Workbook workbook) {
        // Используем переданный workbook
        List<LocationsInfoDTO> locations = service.getAllLocationInfo();
        int rowNum = 1;

        for (LocationsInfoDTO location : locations) {
            Row row = sheet.createRow(rowNum++);
            // ... заполнение данных
            row.createCell(0).setCellValue(location.location_name());
            row.createCell(1).setCellValue(location.phone_number());
            row.createCell(2).setCellValue(location.country());
            row.createCell(3).setCellValue(location.city());
            row.createCell(4).setCellValue(location.street());
            row.createCell(5).setCellValue(location.building_number());
            row.createCell(6).setCellValue(location.postal_code());
            row.createCell(7).setCellValue(location.location_type_name());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 8; i++) {
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
        grid.setItems(service.getAllLocationInfo());
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
                    //resource.getUrl(),
                    resource.getName()
            );
        }
    }
}