package com.example.application.views.reports;

import com.example.application.reports.orders.OrderInfoDTO;
import com.example.application.reports.orders.OrderInfoService;
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
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@PageTitle("Отчет по заказам")
@Route("orders-info")
@Menu(order = 47, icon = LineAwesomeIconUrl.RECEIPT_SOLID)
@RolesAllowed({"MANAGER", "ANALYSTS", "GOD"})
public class OrderInfoView extends VerticalLayout {
    private final transient OrderInfoService service;
    private final Grid<OrderInfoDTO> grid = new Grid<>(OrderInfoDTO.class, false);

    public OrderInfoView(OrderInfoService service) {
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
        grid.addColumn(OrderInfoDTO::locationName).setHeader("Офис");
        grid.addColumn(OrderInfoDTO::fullName).setHeader("ФИО мастера");
        grid.addColumn(OrderInfoDTO::orderNumber).setHeader("Номер заказа");
        grid.addColumn(dto -> dto.orderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setHeader("Дата заказа");

        grid.addColumn(new NumberRenderer<>(
                OrderInfoDTO::totalCost,
                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
        )).setHeader("Стоимость работ и комплектующих").setTextAlign(ColumnTextAlign.END);

        grid.addColumn(new NumberRenderer<>(
                OrderInfoDTO::discountedCost,
                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
        )).setHeader("Итоговая сумма").setTextAlign(ColumnTextAlign.END);

        grid.addColumn(new NumberRenderer<>(
                OrderInfoDTO::deductedBonuses,
                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
        )).setHeader("Оплачено бонусами").setTextAlign(ColumnTextAlign.END);

        grid.addColumn(new NumberRenderer<>(
                OrderInfoDTO::accruedBonuses,
                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
        )).setHeader("Начислено бонусов").setTextAlign(ColumnTextAlign.END);

        grid.addColumn(OrderInfoDTO::orderStatus).setHeader("Статус заказа");
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
            StreamResource resource = new StreamResource("orders.xlsx", () -> {
                try (Workbook workbook = new XSSFWorkbook();
                     ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                    Sheet sheet = workbook.createSheet("Orders");
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
                    "orders.xlsx"
            );

        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Офис", "ФИО мастера", "Номер заказа", "Дата заказа",
                "Стоимость работ и комплектующих", "Итоговая сумма", "Оплачено бонусами",
                "Начислено бонусов", "Статус заказа"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    private void fillDataRows(Sheet sheet, Workbook workbook) {
        List<OrderInfoDTO> orders = service.getAllOrderInfo();
        int rowNum = 1;

        for (OrderInfoDTO order : orders) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.locationName());
            row.createCell(1).setCellValue(order.fullName());
            row.createCell(2).setCellValue(order.orderNumber());
            row.createCell(3).setCellValue(order.orderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            // Handle totalCost with null check
            BigDecimal totalCost = order.totalCost();
            row.createCell(4).setCellValue(totalCost != null ? totalCost.doubleValue() : 0.0);

            // Handle discountedCost with null check
            BigDecimal discountedCost = order.discountedCost();
            row.createCell(5).setCellValue(discountedCost != null ? discountedCost.doubleValue() : 0.0);

            // Handle deductedBonuses with null check
            BigDecimal deductedBonuses = order.deductedBonuses();
            row.createCell(6).setCellValue(deductedBonuses != null ? deductedBonuses.doubleValue() : 0.0);

            // Handle accruedBonuses with null check
            BigDecimal accruedBonuses = order.accruedBonuses();
            row.createCell(7).setCellValue(accruedBonuses != null ? accruedBonuses.doubleValue() : 0.0);
            row.createCell(8).setCellValue(order.orderStatus());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void refreshGrid() {
        grid.setItems(service.getAllOrderInfo());
    }
}