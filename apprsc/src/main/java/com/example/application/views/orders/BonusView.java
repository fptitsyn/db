package com.example.application.views.orders;

import com.example.application.data.BonusAccount;
import com.example.application.data.Clients;
import com.example.application.data.Orders;
import com.example.application.services.BonusAccountService;
import com.example.application.services.ClientsService;
import com.example.application.services.OrdersService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@PageTitle("Бонусный счет")
@RolesAllowed({"SALES","GOD"})
@Route(value = "bonus/:clientID")
public class BonusView extends VerticalLayout implements BeforeEnterObserver {
    private final OrdersService orderService;
    private final ClientsService clientService;
    private final BonusAccountService bonusAccountService; // Добавили сервис для BonusAccount
    private Clients currentClient;
    private Grid<Orders> orderGrid = new Grid<>(Orders.class);
    private Span clientFullname = new Span();
    private Span accountNumberSpan = new Span();
    private Span openDateSpan = new Span();

    // Внедряем BonusAccountService через конструктор
    public BonusView(OrdersService orderService, ClientsService clientService, BonusAccountService bonusAccountService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.bonusAccountService = bonusAccountService;
        initView();
    }

    private void initView() {
        accountNumberSpan.addClassName("bonus-info");
        openDateSpan.addClassName("bonus-info");

        orderGrid.removeAllColumns();
        orderGrid.addColumn(Orders::getProduct).setHeader("Товар");
        orderGrid.addColumn(Orders::getQuantity).setHeader("Количество");

        add(
                clientFullname,
                new H3("Информация о бонусном счете"),
                accountNumberSpan, openDateSpan,
                new H3("Начисления и списания"),
                orderGrid
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
            clientFullname.setText("Клиент: " + currentClient.getName());
            clientFullname.addClassName("client-name");

            // Получаем BonusAccount для текущего клиента
            updateBonusAccountInfo();
            updateGrid();

        } catch (NumberFormatException e) {
            Notification.show("Неверный ID клиента", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void updateBonusAccountInfo() {
        // Ищем BonusAccount по clientId
        Optional<BonusAccount> bonusAccount = bonusAccountService.findByClientId(currentClient.getId());

        if (bonusAccount.isPresent()) {
            BonusAccount account = bonusAccount.get();
            accountNumberSpan.setText("Номер счета: " + account.getBonusAccountNumber());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = account.getOpenDate().format(formatter);
            openDateSpan.setText("Дата открытия: " + formattedDate);
        } else {
            accountNumberSpan.setText("Бонусный счет не найден");
            openDateSpan.setText("");
        }
    }

    private void updateGrid() {
        orderGrid.setItems(orderService.findByClientId(currentClient.getId()));
    }
}
