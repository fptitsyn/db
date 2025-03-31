package com.example.application.views.orders;

import com.example.application.data.Clients;
import com.example.application.data.components.ComponentDTO;
import com.example.application.services.ClientsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Route(value = "all-clients-view")
@PageTitle("Список клиентов")
@Menu(order = 11, icon = LineAwesomeIconUrl.ADDRESS_BOOK_SOLID)

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
        Grid.Column<Clients> nameCol = clientGrid.addColumn(Clients::getFullName)
                .setHeader("Имя")
                .setKey("name")
                .setSortable(true);

        clientGrid.addComponentColumn(this::createActions).setHeader("Действия");

        // Настройка сортировки по умолчанию
        List<GridSortOrder<Clients>> sortOrder = Arrays.asList(
                new GridSortOrder<>(nameCol, SortDirection.ASCENDING)
        );
        clientGrid.sort(sortOrder);

        // Кнопка добавления нового клиента
        Button addClientBtn = new Button("Новый", VaadinIcon.PLUS_SQUARE_O.create(), e -> showClientForm(new Clients()));

        // Обновление данных
        updateGrid();

        add(addClientBtn, clientGrid);
    }

    private void updateGrid() {
        clientGrid.setItems(clientService.findAll());
    }

    private HorizontalLayout createActions(Clients client) {
        Button editBtn = new Button("Редактировать", VaadinIcon.EDIT.create(), e -> showClientForm(client));
        Button ordersBtn = new Button("Заказы", VaadinIcon.CART.create(), e -> showOrders(client));
        Button bonusBtn = new Button("Бонусы", VaadinIcon.GIFT.create(), e -> showBonus(client));
        Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), e -> {
            clientService.delete(client);
            updateGrid();
        });

        return new HorizontalLayout(editBtn, deleteBtn, ordersBtn, bonusBtn);
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
    private void showBonus(Clients client) {
        getUI().ifPresent(ui -> {
            // Создаем параметры маршрута через Map
            RouteParameters parameters = new RouteParameters(
                    Collections.singletonMap("clientID", String.valueOf(client.getId()))
            );

            // Переход на BonusView с параметрами
            ui.navigate(BonusView.class, parameters);
        });
    }
}