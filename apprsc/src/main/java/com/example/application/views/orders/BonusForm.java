package com.example.application.views.orders;

import com.example.application.data.orders.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@PageTitle("Бонусный счет")
@RolesAllowed({"SALES", "GOD"})
@Route(value = "bonus/:clientID")
public class BonusForm extends VerticalLayout implements BeforeEnterObserver {
    private final ClientsService clientService;
    private final BonusAccountService bonusAccountService; // Добавили сервис для BonusAccount
    private final BonusAccountOperationService bonusAccountOperationService;
    private final Grid<BonusAccountOperation> bonusOperationGrid = new Grid<>(BonusAccountOperation.class);
    private final Span clientFullName = new Span();
    private final Span accountNumberSpan = new Span();
    private final Span openDateSpan = new Span();
    private final Button backButton = new Button("Вернуться к списку клиентов");
    private Clients currentClient;
    private Grid.Column<BonusAccountOperation> bonusesColumn;

    // Внедряем BonusAccountService через конструктор
    public BonusForm(ClientsService clientService, BonusAccountService bonusAccountService, BonusAccountOperationService bonusAccountOperationService) {
        this.clientService = clientService;
        this.bonusAccountService = bonusAccountService;
        this.bonusAccountOperationService = bonusAccountOperationService;
        initView();
    }

    private void initView() {
        configureBackButton(); // Настройка кнопки
        accountNumberSpan.addClassName("bonus-info");
        openDateSpan.addClassName("bonus-info");

        bonusOperationGrid.removeAllColumns();
        bonusOperationGrid.addColumn(BonusAccountOperation::getOperationType).setHeader("Тип операции");
        bonusesColumn = bonusOperationGrid.addColumn(BonusAccountOperation::getOperationSumm).setHeader("Бонусы").setTextAlign(ColumnTextAlign.END);
        bonusOperationGrid.addColumn(BonusAccountOperation::getOperationDate).setHeader("Дата операции");
        bonusOperationGrid.addColumn(BonusAccountOperation::getOrderNumDate).setHeader("Основание").setWidth("25rem");
        bonusOperationGrid.setEmptyStateText("Нет начисленных бонусов");
        setSizeFull();
        add(
                clientFullName,
                new H3("Информация о бонусном счете"),
                accountNumberSpan, openDateSpan,
                new H3("Начисления и списания"),
                bonusOperationGrid,
                backButton
        );
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
            clientFullName.setText("Клиент: " + currentClient.getFullName());
            clientFullName.addClassName("client-name");

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
        Long bonusAccountId = bonusAccountService.findByClientId(currentClient.getId())
                .orElseThrow(() -> new RuntimeException("Bonus account not found"))
                .getId();
        bonusOperationGrid.setItems(bonusAccountOperationService.findAllByBonusAccountId(bonusAccountId));
        updateFooters();
    }

    private void updateFooters() {
        Long bonusAccountId = bonusAccountService.findByClientId(currentClient.getId())
                .orElseThrow(() -> new RuntimeException("Bonus account not found"))
                .getId();
        BigDecimal totalBonuses = bonusAccountOperationService.getTotalBonuses(bonusAccountId);
        bonusesColumn.setFooter(String.format("Доступно %,.2f бонусов", totalBonuses));
    }
}
