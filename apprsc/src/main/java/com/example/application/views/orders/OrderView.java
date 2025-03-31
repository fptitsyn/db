
package com.example.application.views.orders;

import com.example.application.data.*;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.ClientsService;
import com.example.application.services.EmployeesMovingService;
import com.example.application.services.OrdersService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

@RolesAllowed({"SALES","GOD"})
@Route(value = "orders/:clientID") // Убрали layout

public class OrderView extends VerticalLayout implements BeforeEnterObserver {
    private final AuthenticatedUser authenticatedUser;
    private final OrdersService orderService;
    private final ClientsService clientService;
    private final EmployeesMovingService employeesMovingService;
    private Clients currentClient;
    private Grid<Orders> orderGrid = new Grid<>(Orders.class);
    private Span clientFullname = new Span();

    public OrderView(OrdersService orderService,
                     ClientsService clientService,
                     AuthenticatedUser authenticatedUser,
                     EmployeesMovingService employeesMovingService) { // Модифицированный конструктор
        this.orderService = orderService;
        this.clientService = clientService;
        this.authenticatedUser = authenticatedUser;
        this.employeesMovingService = employeesMovingService; // Инициализация
        initView();
    }

    private void initView() {
        orderGrid.removeAllColumns();

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
        }).setHeader("Сотрудник").setAutoWidth(true);

        orderGrid.addColumn(o -> o.getProduct()).setHeader("Товар");
        orderGrid.addColumn(o -> o.getQuantity()).setHeader("Количество");
        orderGrid.addComponentColumn(this::createOrderActions).setHeader("Действия");

        Button addOrderBtn = new Button("Новый", VaadinIcon.PLUS_SQUARE_O.create(), e -> showOrderForm(new Orders()));
        add(clientFullname,new H3("Заказы клиента"), addOrderBtn, orderGrid);
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
    }

    private HorizontalLayout createOrderActions(Orders order) {
        Button editBtn = new Button("Редактировать", e -> showOrderForm(order));
        Button deleteBtn = new Button("Удалить", e -> {
            orderService.delete(order);
            updateGrid();
        });
        return new HorizontalLayout(editBtn, deleteBtn);
    }

    private void showOrderForm(Orders order) {
        if (order.getId() == null) { // Только для новых заказов
            Optional<Users> maybeUser = authenticatedUser.get();
            if (maybeUser.isEmpty()) {
                Notification.show("Ошибка: Пользователь не найден", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Users user = maybeUser.get();
            Employees employee = user.getEmployee();
            if (employee == null) {
                Notification.show("Ошибка: У пользователя нет привязанного сотрудника. Обратитесь к администратору", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            // Поиск активного назначения
            Optional<EmployeesMoving> activeMoving = employeesMovingService.findActiveByEmployee(employee);
            if (activeMoving.isEmpty()) {
                Notification.show("Ошибка: Сотрудник не принят на работу. Не может оформлять заказы", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            // Получаем локацию из штатного расписания
            StaffingTable staffingTable = activeMoving.get().getStaffingTable();
            Locations location = staffingTable.getLocation();
            if (location == null) {
                Notification.show("Ошибка: Офис не найден в штатном расписании", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            // Устанавливаем данные в заказ
            order.setEmployee(employee);
            order.setLocation(location);
        }

        OrderForm form = new OrderForm(
                order,
                currentClient,
                orderService,
                this::updateGrid
        );
        add(form);
    }
}

