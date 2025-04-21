package com.example.application.views.reports;

import com.example.application.reports.workOrders.WorkOrderInfoDTO;
import com.example.application.reports.workOrders.WorkOrderInfoService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Отчет по нарядам")
@Route("workorders-info")
@Menu(order = 46, icon = LineAwesomeIconUrl.CLIPBOARD_LIST_SOLID)
@RolesAllowed({"MANAGER", "ANALYSTS", "GOD"})
public class WorkOrderInfoView extends VerticalLayout {
    private final transient WorkOrderInfoService service;
    private final Grid<WorkOrderInfoDTO> grid = new Grid<>(WorkOrderInfoDTO.class, false);

    public WorkOrderInfoView(WorkOrderInfoService service) {
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
        grid.addColumn(WorkOrderInfoDTO::fullName).setHeader("ФИО мастера");
        grid.addColumn(WorkOrderInfoDTO::orderNumber).setHeader("Номер наряда");
        grid.addColumn(dto -> dto.orderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setHeader("Дата наряда");
        grid.addColumn(WorkOrderInfoDTO::servicesAmount).setHeader("Количество услуг");
        grid.addColumn(WorkOrderInfoDTO::hoursAmount).setHeader("Количество часов");
        grid.addColumn(WorkOrderInfoDTO::orderStatus).setHeader("Статус наряда");
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
            StreamResource resource = new StreamResource("workorders.xlsx", () -> {
                try (Workbook workbook = new XSSFWorkbook();
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                    Sheet sheet = workbook.createSheet("Work Orders");
                    createHeaderRow(sheet);
                    fillDataRows(sheet, workbook);
                    autoSizeColumns(sheet);

                    workbook.write(out);
                    return new ByteArrayInputStream(out.toByteArray());

                } catch (IOException e) {
                    throw new RuntimeException("Excel creation failed", e);
                }
            });

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
                    "workorders.xlsx"
            );

        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ФИО мастера", "Номер наряда", "Дата наряда",
                "Количество услуг", "Количество часов", "Статус наряда"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    private void fillDataRows(Sheet sheet, Workbook workbook) {
        List<WorkOrderInfoDTO> workOrders = service.getAllWorkOrderInfo();
        int rowNum = 1;

        for (WorkOrderInfoDTO order : workOrders) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.fullName());
            row.createCell(1).setCellValue(order.orderNumber());
            row.createCell(2).setCellValue(order.orderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            row.createCell(3).setCellValue(order.servicesAmount());
            row.createCell(4).setCellValue(order.hoursAmount());
            row.createCell(5).setCellValue(order.orderStatus());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void refreshGrid() {
        grid.setItems(service.getAllWorkOrderInfo());
    }
}