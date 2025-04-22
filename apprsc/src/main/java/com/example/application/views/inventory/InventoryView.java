package com.example.application.views.inventory;

import com.example.application.data.components.Component;
import com.example.application.data.components.ComponentRepository;
import com.example.application.data.inventory.*;
import com.example.application.data.locations.Locations;
import com.example.application.data.locations.LocationsRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Comparator;

@Route(value = "inventory")
@PageTitle("Склад")
@Menu(order = 44, icon = LineAwesomeIconUrl.USER_FRIENDS_SOLID)
@RolesAllowed({"SALES", "GOD"})
public class InventoryView extends VerticalLayout {

    private final InventoryService inventoryService;
    private final ComponentRepository componentRepo;
    private final LocationsRepository locationsRepo;
    private final InventoryReceiptService inventoryReceiptService;
    private final InventoryIssueService inventoryIssueService;

    private final Button addButton = new Button("Принять на склад", VaadinIcon.PLUS_SQUARE_O.create());
    private final Button receiptListButton = new Button("Приходные операции", VaadinIcon.LIST_UL.create());
    private final Button issueListButton = new Button("Расходные операции", VaadinIcon.LIST_UL.create());

    private final Grid<Inventory> grid = new Grid<>(Inventory.class);

    public InventoryView(InventoryService inventoryService,
                         ComponentRepository componentRepo,
                         LocationsRepository locationsRepo,
                         InventoryReceiptService inventoryReceiptService,
                         InventoryIssueService inventoryIssueService) {
        this.inventoryService = inventoryService;
        this.componentRepo = componentRepo;
        this.locationsRepo = locationsRepo;
        this.inventoryReceiptService = inventoryReceiptService;
        this.inventoryIssueService = inventoryIssueService;

        initView();

    }

    private void initView() {
        styleButton(addButton, "primary");
        styleButton(receiptListButton, "primary");
        styleButton(issueListButton, "primary");
        addButton.addClickListener(ignored -> {
            InventoryView.ReceiptForm editor = new InventoryView.ReceiptForm();
            editor.open();
        });
        receiptListButton.addClickListener(ignored -> {
            InventoryView.ReceiptInventoryForm editor = new InventoryView.ReceiptInventoryForm();
            editor.open();
        });
        issueListButton.addClickListener(ignored -> {
            InventoryView.IssueInventoryForm editor = new InventoryView.IssueInventoryForm();
            editor.open();
        });

        setSizeFull();
        add(new HorizontalLayout(addButton, receiptListButton, issueListButton));
        createInventoryGrid();
        refreshGrid();
    }

    private void createInventoryGrid() {
        grid.removeAllColumns();
        grid.addColumn(inv -> inv.getLocations().getName()).setHeader("Офис");
        grid.addColumn(inv -> inv.getComponent().getName()).setHeader("Комплектующая");
        grid.addColumn(Inventory::getQuantity).setHeader("Количество");
        add(new H3("Текущие остатки"), grid);
    }

    private void refreshGrid() {
        grid.setItems(inventoryService.getAllInventoryItems());
    }

    private class ReceiptForm extends Dialog {
        private ReceiptForm() {
            setWidth("600px");

            ComboBox<Component> componentCombo = new ComboBox<>("Комплектующая");
            componentCombo.setItems(componentRepo.findAll()
                    .stream()
                    .sorted(Comparator.comparing(Component::getComponentCategoryName)) // Сортировка по имени
                    .toList()
            );
            componentCombo.setItemLabelGenerator(Component::getComponentCategoryName);
            componentCombo.setWidthFull();

            ComboBox<Locations> locationCombo = new ComboBox<>("Офис");
            locationCombo.setItems(locationsRepo.findAll());
            locationCombo.setItemLabelGenerator(Locations::getName);
            locationCombo.setWidthFull();

            IntegerField quantityField = new IntegerField("Количество");
            quantityField.setWidthFull();

            Button submit = new Button("Принять на склад", VaadinIcon.PLUS_SQUARE_O.create(), ignored -> {
                try {
                    inventoryReceiptService.receiveComponent(
                            componentCombo.getValue().getComponentId(),
                            locationCombo.getValue().getId(),
                            quantityField.getValue()
                    );

                    close();
                    refreshGrid();
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            });

            Button closeBtn = new Button("Отмена", VaadinIcon.CLOSE.create(), ignored -> close());

            styleButton(submit, "primary");
            styleButton(closeBtn, "error");

            VerticalLayout layout = new VerticalLayout(
                    new H3("Приём на склад"),
                    componentCombo,
                    locationCombo,
                    quantityField,
                    new HorizontalLayout(submit, closeBtn) {{
                        setWidthFull();
                        setJustifyContentMode(JustifyContentMode.END);
                    }}
            );
            layout.setPadding(true);
            layout.setSpacing(true);
            layout.setWidthFull();

            add(layout);
        }
    }

    private class ReceiptInventoryForm extends Dialog {
        private final Grid<InventoryReceipt> gridReceipt = new Grid<>(InventoryReceipt.class);

        private ReceiptInventoryForm() {
            Button closeBtn = new Button("Отмена", VaadinIcon.CLOSE.create(), ignored -> close());
            styleButton(closeBtn, "error");

            createInventoryReceiptGrid();

            VerticalLayout layout = new VerticalLayout(
                    new H3("Принято на склад"),
                    gridReceipt,
                    new HorizontalLayout(closeBtn) {{
                        setWidthFull();
                        setJustifyContentMode(JustifyContentMode.END);
                    }}
            );

            add(layout);
        }
        private void createInventoryReceiptGrid(){
            gridReceipt.removeAllColumns();
            gridReceipt.addColumn(InventoryReceipt::getReceiptDate).setHeader("Дата приёма");
            gridReceipt.addColumn(inv -> inv.getLocations().getName()).setHeader("Офис");
            gridReceipt.addColumn(inv -> inv.getComponent().getName()).setHeader("Комплектующая");
            gridReceipt.addColumn(InventoryReceipt::getQuantity).setHeader("Количество");
            gridReceipt.setItems(inventoryReceiptService.getAllInventoryReceiptItems());

            gridReceipt.setWidth("800px");
            gridReceipt.setHeight("600px");
        }
    }

    private class IssueInventoryForm extends Dialog {
        private final Grid<InventoryIssue> gridIssue = new Grid<>(InventoryIssue.class);
        private IssueInventoryForm() {
            Button closeBtn = new Button("Отмена", VaadinIcon.CLOSE.create(), ignored -> close());
            styleButton(closeBtn, "error");

            createInventoryIssueGrid();

            VerticalLayout layout = new VerticalLayout(
                    new H3("Выдано со склада"),
                    gridIssue,
                    new HorizontalLayout(closeBtn) {{
                        setWidthFull();
                        setJustifyContentMode(JustifyContentMode.END);
                    }}
            );

            add(layout);
        }
        private void createInventoryIssueGrid(){
            gridIssue.removeAllColumns();
            gridIssue.addColumn(InventoryIssue::getIssueDate).setHeader("Дата выдачи");
            gridIssue.addColumn(inv -> inv.getLocations().getName()).setHeader("Офис");
            gridIssue.addColumn(inv -> inv.getComponent().getName()).setHeader("Комплектующая");
            gridIssue.addColumn(InventoryIssue::getQuantity).setHeader("Количество");
            gridIssue.addColumn(inv -> inv.getOrder().getNumberOfOrder()).setHeader("Номер заказа");
            gridIssue.setItems(inventoryIssueService.getAllInventoryIssueItems());

            gridIssue.setWidth("800px");
            gridIssue.setHeight("600px");
        }
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
}