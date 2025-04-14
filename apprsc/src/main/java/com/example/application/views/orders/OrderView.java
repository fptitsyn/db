package com.example.application.views.orders;

import com.example.application.data.components.ComponentService;
import com.example.application.data.employees.*;
import com.example.application.data.locations.Locations;
import com.example.application.data.login.Users;
import com.example.application.data.orders.*;
import com.example.application.data.services.ServicesService;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

@PageTitle("Заказы")
@RolesAllowed({"SALES", "GOD"})
@Route(value = "orders/:clientID")

public class OrderView extends VerticalLayout implements BeforeEnterObserver {
    private final AuthenticatedUser authenticatedUser;
    private final OrdersService orderService;
    private final ClientsService clientService;
    private final EmployeesMovingService employeesMovingService;
    private final OrderServicesService orderServicesService; // Добавлено
    private final ServicesService servicesService;
    private final OrderComponentsService orderComponentsService; // Добавлено
    private final ComponentService componentService;
    private final WorkOrdersService workOrdersService;
    private final EmployeesService employeesService;
    private final BonusAccountService bonusAccountService;
    private final BonusAccountOperationService bonusAccountOperationService;
    private final ClientStatusService clientStatusService;
    private final InvoiceForPaymentService invoiceForPaymentService;
    private final Grid<Orders> orderGrid = new Grid<>(Orders.class);
    private final Span clientFullName = new Span();
    private final Button backButton = new Button("Вернуться к списку клиентов");
    private Clients currentClient;

    public OrderView(OrdersService orderService,
                     ClientsService clientService,
                     AuthenticatedUser authenticatedUser,
                     EmployeesMovingService employeesMovingService,
                     OrderServicesService orderServicesService,
                     ServicesService servicesService,
                     OrderComponentsService orderComponentsService,
                     ComponentService componentService,
                     WorkOrdersService workOrdersService,
                     EmployeesService employeesService,
                     BonusAccountService bonusAccountService,
                     BonusAccountOperationService bonusAccountOperationService,
                     ClientStatusService clientStatusService,
                     InvoiceForPaymentService invoiceForPaymentService
    ) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.authenticatedUser = authenticatedUser;
        this.employeesMovingService = employeesMovingService;
        this.orderServicesService = orderServicesService;
        this.servicesService = servicesService;
        this.orderComponentsService = orderComponentsService;
        this.componentService = componentService;
        this.workOrdersService = workOrdersService;
        this.employeesService = employeesService;
        this.bonusAccountService = bonusAccountService;
        this.bonusAccountOperationService = bonusAccountOperationService;
        this.clientStatusService = clientStatusService;
        this.invoiceForPaymentService = invoiceForPaymentService;

        initView();
    }

    private void initView() {
        configureBackButton(); // Настройка кнопки
        orderGrid.removeAllColumns();

        orderGrid.addColumn(Orders::getDateOfOrder).setHeader("Дата");
        orderGrid.addColumn(Orders::getNumberOfOrder).setHeader("Номер");
        orderGrid.addColumn(Orders::getTotalCost).setHeader("Сумма").setTextAlign(ColumnTextAlign.END);
        orderGrid.addColumn(Orders::getOrderStatusName).setHeader("Статус");

        orderGrid.addColumn(order -> {
            if (order.getLocation() != null) {
                return order.getLocation().getName();
            }
            return "Не указано";
        }).setHeader("Офис").setAutoWidth(true);


        orderGrid.addColumn(order -> {
            if (order.getEmployee() != null) {
                return order.getEmployee().getFullName();
            }
            return "Не указан";
        }).setHeader("Менеджер").setAutoWidth(true);


        orderGrid.addComponentColumn(this::createOrderActions).setHeader("Действия").setWidth("125px");

        Button addOrderBtn = new Button("Новый", VaadinIcon.PLUS_SQUARE_O.create(), ignored -> showOrderForm(new Orders()));
        addOrderBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addOrderBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        setSizeFull();
        add(clientFullName, addOrderBtn, orderGrid, backButton);


    }

    private void configureBackButton() {
        backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());

        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        backButton.addClickListener(ignored ->
                getUI().ifPresent(ui -> ui.navigate(ClientsView.class))
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RouteParameters params = event.getRouteParameters();
        String clientID = params.get("clientID").orElse("0");

        try {
            Long id = Long.parseLong(clientID);
            currentClient = clientService.findById(id)
                    .orElseThrow(() -> new NotFoundException("Client not found"));
            // Устанавливаем имя клиента
            clientFullName.setText("Клиент: " + currentClient.getFullName());
            clientFullName.addClassName("client-name");

            updateGrid();
        } catch (NumberFormatException e) {
            Notification.show("Invalid client ID", 3000, Notification.Position.TOP_CENTER);
        }
    }


    private void updateGrid() {
        orderGrid.setItems(orderService.findByClientId(currentClient.getId()));
        orderGrid.getDataProvider().refreshAll(); // Принудительное обновление данных
    }

    private HorizontalLayout createOrderActions(Orders order) {
        Button editBtn = new Button("Открыть", VaadinIcon.EDIT.create(), ignored -> showOrderForm(order));
        editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return new HorizontalLayout(editBtn);

    }private void showOrderForm(Orders order) {
        // Проверки сотрудника и локации для новых заказов
        if (order.getId() == null) {
            Optional<Users> maybeUser = authenticatedUser.get();
            if (maybeUser.isEmpty()) {
                Notification.show("Ошибка: Пользователь не найден", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Users user = maybeUser.get();
            Employees employee = user.getEmployee();
            if (employee == null) {
                Notification.show("Ошибка: У пользователя нет привязанного сотрудника", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Optional<EmployeesMoving> activeMoving = employeesMovingService.findActiveByEmployee(employee);
            if (activeMoving.isEmpty()) {
                Notification.show("Ошибка: Сотрудник не принят на работу", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            StaffingTable staffingTable = activeMoving.get().getStaffingTable();
            Locations location = staffingTable.getLocation();
            if (location == null) {
                Notification.show("Ошибка: Офис не найден", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            // Устанавливаем значения
            order.setEmployee(employee);
            order.setLocation(location);
        }

        // Создание и отображение диалога
        Dialog dialog = createOrderDialog(order, currentClient);
        dialog.open();
    }

    private Dialog createOrderDialog(Orders order, Clients client) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        dialog.setHeaderTitle(getDialogTitle(order));

        OrderForm form = createOrderForm(order, client, dialog);

        dialog.setWidth("1500px");
        dialog.add(form);
        return dialog;
    }

    private String getDialogTitle(Orders order) {
        return order.getId() == null
                ? "Новый заказ"
                : String.format("Редактирование заказа #%s от %s, статус: %s",
                order.getNumberOfOrder(),
                order.getDateOfOrder(),
                order.getOrderStatusName());
    }

    private OrderForm createOrderForm(Orders order, Clients client, Dialog dialog) {
        return new OrderForm(
                order,
                client,
                orderService,
                orderServicesService,
                servicesService,
                orderComponentsService,
                componentService,
                workOrdersService,
                employeesService,
                bonusAccountService,
                bonusAccountOperationService,
                clientStatusService,
                invoiceForPaymentService,
                createSaveHandler(dialog),
                createCancelHandler(dialog)
        );
    }

    private Runnable createSaveHandler(Dialog dialog) {
        return () -> {
            updateGrid();
            dialog.close();
            Notification.show("Заказ сохранен", 3000, Notification.Position.TOP_CENTER);
        };
    }

    private Runnable createCancelHandler(Dialog dialog) {
        return () -> {
            updateGrid();
            dialog.close();
        };
    }
}

