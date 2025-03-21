package com.example.application.views.orders;

import com.example.application.data.Clients;
import com.example.application.data.Orders;
import com.example.application.services.ClientsService;
import com.example.application.services.OrdersService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"SALES","GOD"})
@Route(value = "orders/:clientID") // Убрали layout

public class OrderView extends VerticalLayout implements BeforeEnterObserver {
    private final OrdersService orderService;
    private final ClientsService clientService;
    private Clients currentClient;
    private Grid<Orders> orderGrid = new Grid<>(Orders.class);

    public OrderView(OrdersService orderService, ClientsService clientService) {
        this.orderService = orderService;
        this.clientService = clientService;
        initView();
    }

    private void initView() {
        orderGrid.removeAllColumns();
        orderGrid.addColumn(o -> o.getProduct()).setHeader("Товар");
        orderGrid.addColumn(o -> o.getQuantity()).setHeader("Количество");
        orderGrid.addComponentColumn(this::createOrderActions).setHeader("Действия");

        Button addOrderBtn = new Button("Добавить заказ", e -> showOrderForm(new Orders()));

        add(new H3("Заказы клиента"), addOrderBtn, orderGrid);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RouteParameters params = event.getRouteParameters();
        String clientID = params.get("clientID").orElse("0");

        try {
            Long id = Long.parseLong(clientID);
            currentClient = clientService.findById(id)
                    .orElseThrow(() -> new NotFoundException("Client not found"));
            updateGrid();
        } catch (NumberFormatException e) {
            Notification.show("Invalid client ID", 3000, Notification.Position.TOP_CENTER);
        }
    }


    private void updateGrid() {
        orderGrid.setItems(orderService.findByClientId(currentClient.getId()));
    }

    private HorizontalLayout createOrderActions(Orders order) {
        Button editBtn = new Button("Ред.", e -> showOrderForm(order));
        Button deleteBtn = new Button("Удалить", e -> {
            orderService.delete(order);
            updateGrid();
        });
        return new HorizontalLayout(editBtn, deleteBtn);
    }

    private void showOrderForm(Orders order) {
        OrderForm form = new OrderForm(
                order,
                currentClient,
                orderService,
                this::updateGrid
        );
        add(form);
    }
}