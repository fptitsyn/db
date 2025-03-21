package com.example.application.views.orders;

import com.example.application.data.Clients;
import com.example.application.services.ClientsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Collections;


@Route(value = "all-clients-view")
@PageTitle("Список клиентов")
@Menu(order = 11, icon = LineAwesomeIconUrl.COLUMNS_SOLID)

@RolesAllowed({"SALES","GOD"})
public class MainView extends VerticalLayout {
    private final ClientsService clientService;
    private final Grid<Clients> clientGrid = new Grid<>(Clients.class);

    public MainView(ClientsService clientService) {
        this.clientService = clientService;
        initView();
    }

    private void initView() {
        // Настройка таблицы клиентов
        clientGrid.removeAllColumns();
        clientGrid.addColumn(Clients::getName).setHeader("Имя клиента");
        clientGrid.addComponentColumn(this::createActions).setHeader("Действия");

        // Кнопка добавления нового клиента
        Button addClientBtn = new Button("Добавить клиента", e -> showClientForm(new Clients()));

        // Обновление данных
        updateGrid();

        add(addClientBtn, clientGrid);
    }

    private void updateGrid() {
        clientGrid.setItems(clientService.findAll());
    }

    private HorizontalLayout createActions(Clients client) {
        Button editBtn = new Button("Ред.", e -> showClientForm(client));
        Button ordersBtn = new Button("Заказы", e -> showOrders(client));
        Button deleteBtn = new Button("Удалить", e -> {
            clientService.delete(client);
            updateGrid();
        });

        return new HorizontalLayout(editBtn, ordersBtn, deleteBtn);
    }

    private void showClientForm(Clients client) {
        ClientForm form = new ClientForm(client, clientService, this::updateGrid);
        add(form);
        form.setVisible(true);
    }

    private void showOrders(Clients client) {
        getUI().ifPresent(ui -> {
            // Создаем параметры маршрута через Map
            RouteParameters parameters = new RouteParameters(
                    Collections.singletonMap("clientID", String.valueOf(client.getId()))
            );

            // Переход на OrderView с параметрами
            ui.navigate(OrderView.class, parameters);
        });
    }
}