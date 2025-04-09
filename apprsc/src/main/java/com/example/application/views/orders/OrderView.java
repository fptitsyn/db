
package com.example.application.views.orders;

import com.example.application.data.components.ComponentService;
import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesMoving;
import com.example.application.data.locations.Locations;
import com.example.application.data.employees.StaffingTable;
import com.example.application.data.login.Users;
import com.example.application.data.orders.*;
import com.example.application.security.AuthenticatedUser;
import com.example.application.data.employees.EmployeesMovingService;
import com.example.application.data.services.ServicesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
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
@RolesAllowed({"SALES","GOD"})
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

    private Clients currentClient;
    private Grid<Orders> orderGrid = new Grid<>(Orders.class);
    private Span clientFullname = new Span();
    private Button backButton = new Button("Вернуться к списку клиентов");

    public OrderView(OrdersService orderService,
                     ClientsService clientService,
                     AuthenticatedUser authenticatedUser,
                     EmployeesMovingService employeesMovingService,
                     OrderServicesService orderServicesService,
                     ServicesService servicesService,
                     OrderComponentsService orderComponentsService,
                     ComponentService componentService)  { // Модифицированный конструктор
        this.orderService = orderService;
        this.clientService = clientService;
        this.authenticatedUser = authenticatedUser;
        this.employeesMovingService = employeesMovingService;
        this.orderServicesService = orderServicesService;
        this.servicesService = servicesService;
        this.orderComponentsService = orderComponentsService;
        this.componentService = componentService;

        initView();
    }

    private void initView() {
        configureBackButton(); // Настройка кнопки
        orderGrid.removeAllColumns();

        orderGrid.addColumn(o -> o.getDateOfOrder()).setHeader("Дата");
        orderGrid.addColumn(o -> o.getNumberOfOrder()).setHeader("Номер");
        orderGrid.addColumn(o -> o.getTotalCost()).setHeader("Сумма");
        orderGrid.addColumn(o -> o.getOrderStatusName()).setHeader("Статус");

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


        orderGrid.addComponentColumn(this::createOrderActions).setHeader("Действия").setWidth("250px");

        Button addOrderBtn = new Button("Новый", VaadinIcon.PLUS_SQUARE_O.create(), e -> showOrderForm(new Orders()));
        addOrderBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addOrderBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        add(clientFullname, addOrderBtn, orderGrid, backButton);


    }
    private void configureBackButton() {
        backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());

        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        backButton.addClickListener(e ->
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
            clientFullname.setText("Клиент: " + currentClient.getFullName());
            clientFullname.addClassName("client-name");

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
        Button editBtn = new Button("Изменить", VaadinIcon.EDIT.create(), e -> showOrderForm(order));
        editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), e -> {
            orderService.delete(order);
            updateGrid();
        });
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        return new HorizontalLayout(editBtn, deleteBtn);
    }

    private void showOrderForm(Orders order) {
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

        // Создание диалога
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        dialog.setHeaderTitle(order.getId() == null ? "Новый заказ" : "Редактирование заказа");

        OrderForm form = new OrderForm(
                order,
                currentClient,
                orderService,
                orderServicesService,
                servicesService,
                orderComponentsService,
                componentService,
                () -> {
                    //orderService.refresh(order); // Добавлено: обновляем entity
                    updateGrid();               // Обновляем сетку
                    dialog.close();             // Закрываем диалог
                    Notification.show("Заказ сохранен", 3000, Notification.Position.TOP_CENTER);
                },
                () -> {
                    dialog.close();              // Просто закрываем диалог при отмене
                }
        );
        dialog.setWidth("1500px");
        dialog.add(form);
        dialog.open();
    }
}

