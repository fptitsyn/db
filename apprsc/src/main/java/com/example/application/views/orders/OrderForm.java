package com.example.application.views.orders;

import com.example.application.data.components.Component;
import com.example.application.data.components.ComponentService;
import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.orders.*;
import com.example.application.data.services.Services;
import com.example.application.data.services.ServicesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OrderForm extends VerticalLayout {
    private final Orders order;
    private final Clients currentClient;
    private final OrdersService orderService;
    private final OrderServicesService orderServicesService;
    private final ServicesService servicesService;
    private final OrderComponentsService orderComponentsService;
    private final ComponentService componentService;
    private final WorkOrdersService workOrdersService;
    private final EmployeesService employeesService;
    private final BonusAccountService bonusAccountService;
    private final BonusAccountOperationService bonusAccountOperationService;
    private final ClientStatusService clientStatusService;
    private final InvoiceForPaymentService invoiceForPaymentService;

    //private final Runnable onSave;
    private final Runnable onCloseDialogOrderForm;

    // Основные поля формы
    BigDecimalField orderCost = new BigDecimalField();
    private final TextArea commentField = new TextArea("Комментарий к заказу");

    private final Binder<Orders> binder = new Binder<>(Orders.class);
    // Добавляем переменные для колонок (суммы итого)
    private Grid.Column<OrderServices> costColumn;
    private Grid.Column<OrderServices> timeColumn;
    private Grid.Column<OrderComponents> costComponentsColumn;
    private BigDecimal totalServicesCost = BigDecimal.ZERO;
    private BigDecimal totalComponentsCost = BigDecimal.ZERO;

    // Grid для услуг
    private final Grid<OrderServices> servicesGrid = new Grid<>(OrderServices.class);
    // Grid для компонентов
    private final Grid<OrderComponents> componentsGrid = new Grid<>(OrderComponents.class);

    public OrderForm(Orders order,
                     Clients currentClient,
                     OrdersService orderService,
                     OrderServicesService orderServicesService,
                     ServicesService servicesService,
                     OrderComponentsService orderComponentsService,
                     ComponentService componentService,
                     WorkOrdersService workOrdersService,
                     EmployeesService employeesService,
                     BonusAccountService bonusAccountService,
                     BonusAccountOperationService bonusAccountOperationService,
                     ClientStatusService clientStatusService,
                     InvoiceForPaymentService invoiceForPaymentService,
                     //Runnable onSave,
                     Runnable onCloseDialogOrderForm) {
        this.order = order;
        this.currentClient = currentClient;
        this.orderService = orderService;
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
        //this.onSave = onSave;
        this.onCloseDialogOrderForm = onCloseDialogOrderForm;

        initForm();
    }

    private void initForm() {
        configureBinder();
        configureServicesGrid();
        configureComponentsGrid();

        if (order.getId() == null) {
            order.setClient(currentClient);
        } else {
            orderCost.setValue(order.getTotalCost());
            orderCost.setReadOnly(true);
            Div rubPrefix = new Div();
            rubPrefix.setText("₽");
            orderCost.setPrefixComponent(rubPrefix);
            orderCost.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        }

        commentField.setWidthFull();

        refreshGrids();

        add(new HorizontalLayout(createSaveButton(), createCancelButton(),
                        createAddServiceButton(), createAddComponentButton(),
                        createSetWorkOrderButton(), createPayButton(), createSelectLocationButton(),
                        createCancelOrderButton(), createShowInvoiceForPaymentButton()),
                commentField, servicesGrid, componentsGrid,
                new HorizontalLayout(new Span("Итоговая сумма по заказу к оплате: "), orderCost)
        );


        setSizeFull(); // Для всей формы OrderForm
    }

    private void configureBinder() {
        binder.forField(commentField)
                .asRequired("Введите комментарий к заказу")
                .bind(Orders::getComment, Orders::setComment);

        // Привязка orderCost к полю cost сущности Orders
        binder.forField(orderCost)
                .bind(Orders::getTotalCost, Orders::setTotalCost);

        binder.readBean(order);
    }

    private void configureServicesGrid() {
        servicesGrid.removeAllColumns();
        servicesGrid.setWidthFull();

        servicesGrid.addColumn(os -> os.getServices().getServiceName())
                .setHeader("Услуга");

        // Колонка стоимости с футером
        costColumn = servicesGrid.addColumn(os -> os.getServices().getCost())
                .setHeader("Стоимость").setTextAlign(ColumnTextAlign.END);
        costColumn.setFooter("Итого: 0.00 ₽");

        // Колонка времени с футером
        timeColumn = servicesGrid.addColumn(os -> os.getServices().getTimeToCompleteHours())
                .setHeader("Время выполнения (часы)").setTextAlign(ColumnTextAlign.END);
        timeColumn.setFooter("Итого: 0 ч");


        servicesGrid.addComponentColumn(os -> {
            Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), ignored -> deleteService(os));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteBtn.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            deleteBtn.setVisible(showForNewOrder());
            return deleteBtn;
        }).setHeader("Действия");
        // Обновляем футеры при изменении данных
        servicesGrid.getDataProvider().addDataProviderListener(ignored -> updateFooters());
    }

    private void updateFooters() {
        this.totalServicesCost = servicesGrid.getDataProvider().fetch(new Query<>())
                .map(os -> os.getServices().getCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalTime = servicesGrid.getDataProvider().fetch(new Query<>())
                .mapToInt(os -> os.getServices().getTimeToCompleteHours())
                .sum();

        costColumn.setFooter(String.format("Итого: %,.2f ₽", totalServicesCost));
        timeColumn.setFooter(String.format("Итого: %,d ч", totalTime));

        calculateTotalCost();
    }

    private void refreshServicesGrid() {
        if (order.getId() != null) {
            servicesGrid.setItems(orderServicesService.findByOrderId(order.getId()));
            updateFooters(); // Добавляем вызов обновления
        }
    }

    private void configureComponentsGrid() {
        componentsGrid.removeAllColumns();
        componentsGrid.setWidthFull();

        componentsGrid.addColumn(oc -> oc.getComponent().getCategory().getTypeOfDevice().getTypeOfDeviceName())
                .setHeader("Тип устройства");

        componentsGrid.addColumn(oc -> oc.getComponent().getCategory().getTypeOfPartName())
                .setHeader("Категория компонента");

        componentsGrid.addColumn(oc -> oc.getComponent().getName())
                .setHeader("Название");

        // Сохраняем ссылку на колонку
        costComponentsColumn = componentsGrid.addColumn(oc -> oc.getComponent().getCost())
                .setHeader("Стоимость")
                .setTextAlign(ColumnTextAlign.END);

        costComponentsColumn.setFooter("Итого: 0.00 ₽");

        componentsGrid.addComponentColumn(oc -> {
            Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), ignored -> deleteComponent(oc));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteBtn.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            deleteBtn.setVisible(showForNewOrder());
            return deleteBtn;
        }).setHeader("Действия");

        // Обновляем футеры при изменении данных
        componentsGrid.getDataProvider().addDataProviderListener(ignored -> updateComponentsFooters());
    }

    private void updateComponentsFooters() {
        this.totalComponentsCost = componentsGrid.getDataProvider().fetch(new Query<>())
                .map(oc -> oc.getComponent().getCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        costComponentsColumn.setFooter(String.format("Итого: %,.2f ₽", totalComponentsCost));
        calculateTotalCost();
    }

    private void refreshComponentsGrid() {
        if (order.getId() != null) {
            componentsGrid.setItems(orderComponentsService.findByOrderId(order.getId()));
            updateComponentsFooters(); // Добавляем вызов обновления
        }
    }

    private void calculateTotalCost() {
        BigDecimal totalCost = totalServicesCost.add(totalComponentsCost);
        orderCost.setValue(totalCost);
    }

    private Button createSaveButton() {
        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK.create(), ignored -> save());

        // Устанавливаем видимость кнопки
        saveBtn.setVisible(showForNullOrder());

        saveBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return saveBtn;
    }

    private Button createCancelButton() {
        Button cancelBtn = new Button("Закрыть", VaadinIcon.CLOSE.create(), ignored -> onCloseDialogOrderForm.run());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return cancelBtn;
    }

    private Button createAddServiceButton() {
        Button btn = new Button("Добавить услугу", VaadinIcon.PLUS.create(), ignored -> openAddServiceDialog());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createAddComponentButton() {
        Button btn = new Button("Добавить компонент", VaadinIcon.PLUS.create(), ignored -> openAddComponentDialog());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createSetWorkOrderButton() {
        Button btn = new Button("Передать в работу", VaadinIcon.TOOLS.create(), ignored -> openAddWorkOrderDialog());


        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createPayButton() {
        Button btn = new Button("Оплатить заказ", VaadinIcon.MONEY.create(), ignored -> openPayDialog());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForPayOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createCancelOrderButton() {
        Button btn = new Button("Отменить заказ", VaadinIcon.FILE_REMOVE.create(), ignored -> {
            // Проверяем статус заказа
            if (showForNewOrder()) {
                showCancelConfirmationDialog();
            }
        });

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-error-color)");
        return btn;
    }

    private Button createSelectLocationButton() {
        Button btn = new Button("Где починить?", VaadinIcon.QUESTION_CIRCLE_O.create(), ignored -> openShowAvailableOffices());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createShowInvoiceForPaymentButton() {
        Button btn = new Button("Чек", VaadinIcon.INVOICE.create(), ignored -> openShowInvoice());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForPaidOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private boolean showForNullOrder() {
        // Проверяем что заказ не сохранен (не создан в базе) или статус равен 1
        return order.getId() == null
                || (order.getOrderStatus() != null
                && order.getOrderStatus().getId().equals(1L));
    }

    private boolean showForNewOrder() {
        // Проверяем что заказ сохранен в базе и имеет статус = 1 ('Создан')
        return order.getId() != null
                && order.getOrderStatus() != null
                && order.getOrderStatus().getId().equals(1L);
    }

    private boolean showForPayOrder() {
        // Проверяем что заказ сохранен и статус = Выполнен мастером ('Выполнен')
        return order.getId() != null
                && order.getOrderStatus() != null
                && order.getOrderStatus().getId().equals(3L);
    }

    private boolean showForPaidOrder() {
        return order.getId() != null
                && order.getOrderStatus() != null
                && order.getOrderStatus().getId().equals(4L)
                && !invoiceForPaymentService.findByOrderId(order.getId()).isEmpty();
    }

    private void showCancelConfirmationDialog() {
        if (!binder.writeBeanIfValid(order)) {
            Notification.show("Заполните все обязательные поля");
            return;
        }
        ConfirmDialog confirmDialog = new ConfirmDialog(
                "Подтверждение отмены",
                "Вы уверены, что хотите отменить заказ #" + order.getNumberOfOrder() + "?",
                "Подтвердить отмену", ignored -> {
            try {
                // Сохраняем заказ с обновленными данными
                orderService.save(order);  // <-- Сохраняем изменения из формы

                order.setOrderStatusId(5L); // 5 - ID статуса "Отменен"
                orderService.save(order); //Сохраняем статус
                Notification.show("Заказ #" + order.getNumberOfOrder() + " отменен",
                        3000, Notification.Position.TOP_CENTER);
                onCloseDialogOrderForm.run();
            } catch (Exception ex) {
                Notification.show("Ошибка отмены заказа: " + ex.getMessage(),
                        5000, Notification.Position.TOP_CENTER);
            }
        },
                "Отмена", ignored -> {
        }
        );

        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.open();
    }



    private void save() {
        if (binder.writeBeanIfValid(order)) {
            try {
                // Устанавливаем итоговую стоимость в заказ
                order.setTotalCost(orderCost.getValue());
                orderService.save(order);
                refreshGrids();
                onCloseDialogOrderForm.run();
                Notification.show("Заказ сохранен", 3000, Notification.Position.TOP_CENTER);
            } catch (Exception e) {
                Notification.show("Ошибка сохранения: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void refreshGrids() {
        refreshServicesGrid();
        refreshComponentsGrid();
        // Принудительно обновляем суммы после обновления данных
        updateFooters();
        updateComponentsFooters();
    }

    // Методы для работы с услугами
    private void openAddServiceDialog() {
        if (order.getId() == null) {
            Notification.show("Сначала сохраните основной заказ");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Добавление услуги"); // Добавляем заголовок
        dialog.setWidth("500px");
        ComboBox<Services> combo = new ComboBox<>("Выберите услугу");
        combo.setWidthFull(); // Растягиваем на всю ширину диалога
        combo.setItems(servicesService.findAll());
        combo.setItemLabelGenerator(Services::getServiceName);

        Button addBtn = new Button("Добавить", ignored -> {
            if (combo.getValue() != null) {
                OrderServices os = new OrderServices();
                os.setOrders(order);
                os.setServices(combo.getValue());
                orderServicesService.save(os);
                refreshServicesGrid();
                dialog.close();
            }
        });

        VerticalLayout layout = new VerticalLayout(
                combo,
                new HorizontalLayout(addBtn, new Button("Отмена", ignored -> dialog.close()))
        );
        layout.setPadding(false);
        dialog.add(layout);

        dialog.open();
    }

    private void deleteService(OrderServices os) {
        orderServicesService.delete(os);
        servicesGrid.getDataProvider().refreshAll();
        refreshServicesGrid();
    }

    // Методы для работы с компонентами
    private void openAddComponentDialog() {
        if (order.getId() == null) {
            Notification.show("Сначала сохраните основной заказ");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Добавление компонента"); // Добавляем заголовок
        dialog.setWidth("500px");
        ComboBox<Component> combo = new ComboBox<>("Выберите компонент");
        combo.setWidthFull(); // Растягиваем на всю ширину диалога
        combo.setItems(componentService.findAll());
        combo.setItemLabelGenerator(c ->
                c.getName() + " (" + c.getCategory().getTypeOfPartName() + ")"
        );

        Button addBtn = new Button("Добавить", ignored -> {
            if (combo.getValue() != null) {
                OrderComponents oc = new OrderComponents();
                oc.setOrders(order);
                oc.setComponent(combo.getValue());
                orderComponentsService.save(oc);
                refreshComponentsGrid();
                dialog.close();
            }
        });

        VerticalLayout layout = new VerticalLayout(
                combo,
                new HorizontalLayout(addBtn, new Button("Отмена", ignored -> dialog.close()))
        );
        layout.setPadding(false);
        dialog.add(layout);

        dialog.open();
    }

    private void deleteComponent(OrderComponents oc) {
        orderComponentsService.delete(oc);
        componentsGrid.getDataProvider().refreshAll();
        refreshComponentsGrid();
    }

    // Методы для передачи в работу
    private void openAddWorkOrderDialog() {
        if (!binder.writeBeanIfValid(order)) {
        Notification.show("Заполните все обязательные поля");
        return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Передача в работу");
        dialog.setWidth("500px");

        Button addBtn = new Button("Передать", ignored -> {
            try {
                orderService.save(order);  // <-- Сохраняем изменения из формы

                // Получаем сотрудника с ID = 1
                Employees employee = employeesService.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));

                // Создаем новое рабочее задание
                WorkOrders workOrder = new WorkOrders();
                workOrder.setOrders(order);
                workOrder.setEmployee(employee);

                // Сохраняем рабочее задание (дата и статус установятся триггером)
                workOrdersService.save(workOrder);

                // Обновляем статус заказа
                order.setOrderStatusId(2L);
                orderService.save(order);

                Notification.show("Заказ #" + order.getNumberOfOrder() + " передан в работу",
                        3000, Notification.Position.TOP_CENTER);
                onCloseDialogOrderForm.run();
                dialog.close();
            } catch (Exception ex) {
                Notification.show("Ошибка: " + ex.getMessage(),
                        5000, Notification.Position.TOP_CENTER);
            }
        });

        VerticalLayout layout = new VerticalLayout(
                new HorizontalLayout(addBtn, new Button("Отмена", ignored-> dialog.close()))
        );
        layout.setPadding(false);
        dialog.add(layout);
        dialog.open();
    }

    // Методы для оплаты
    private void openPayDialog() {
        if (!binder.writeBeanIfValid(order)) {
            Notification.show("Заполните все обязательные поля");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Оплата");
        dialog.setWidth("400px");

        Div rubPrefix = new Div();
        rubPrefix.setText("₽");

        // Поле для отображения доступных бонусов
        BigDecimalField totalBonusesField = new BigDecimalField();
        totalBonusesField.setReadOnly(true);
        totalBonusesField.setPrefixComponent(rubPrefix);
        totalBonusesField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        Long bonusAccountId = bonusAccountService.findByClientId(currentClient.getId())
                .orElseThrow(() -> new RuntimeException("Bonus account not found"))
                .getId();
        totalBonusesField.setValue(bonusAccountOperationService.getTotalBonuses(bonusAccountId));

        // Получаем bonusPercentage
        List<ClientStatus> statuses = clientStatusService.findByClientId(currentClient.getId());
        final BigDecimal bonusPercentage = !statuses.isEmpty()
                ? statuses.getFirst().getBonusPercentage() != null
                ? statuses.getFirst().getBonusPercentage()
                : BigDecimal.ZERO // Исправлено на 0% по умолчанию
                : BigDecimal.ZERO;

// Форматируем процент
        String percentageText = bonusPercentage
                .setScale(2, RoundingMode.HALF_UP) + "%";

        // Создаем Span с динамическим текстом
        Span bonusSpan = new Span(" - бонусы для начисления (" + percentageText + ")");

        // Поле для начисления бонусов
        BigDecimalField accruedBonusesField = new BigDecimalField();
        accruedBonusesField.setPrefixComponent(rubPrefix);
        accruedBonusesField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);

        // Расчет значения: (orderCost / 100) * bonusPercentage
        BigDecimal orderCostValue = orderCost.getValue() != null ? orderCost.getValue() : BigDecimal.ZERO;
        BigDecimal accruedValue = orderCostValue
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                .multiply(bonusPercentage)
                .setScale(2, RoundingMode.HALF_UP);
        accruedBonusesField.setValue(accruedValue);

        // Поле для списания бонусов
        BigDecimalField deductedBonusesField = new BigDecimalField();
        deductedBonusesField.setPrefixComponent(rubPrefix);
        deductedBonusesField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        deductedBonusesField.setValue(BigDecimal.ZERO);

        // Валидация списываемых бонусов
        deductedBonusesField.addValueChangeListener(e -> {
            BigDecimal deductedValue = e.getValue() != null ? e.getValue() : BigDecimal.ZERO;
            BigDecimal availableBonuses = totalBonusesField.getValue() != null
                    ? totalBonusesField.getValue()
                    : BigDecimal.ZERO;

            boolean invalid = deductedValue.compareTo(availableBonuses) > 0;
            deductedBonusesField.setInvalid(invalid);

            if (invalid) {
                deductedBonusesField.setErrorMessage("Недостаточно бонусов. Доступно: "
                        + availableBonuses.setScale(2, RoundingMode.HALF_UP));
            }
        });

        // Радио-группа для выбора операции
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioGroup.setLabel("Начисление/списание бонусов");
        radioGroup.setItems("Начислить", "Списать");
        radioGroup.setValue("Начислить"); // Значение по умолчанию

        // Обработчик изменений для radioGroup
        radioGroup.addValueChangeListener(event -> {
            if ("Начислить".equals(event.getValue())) {
                BigDecimal currentOrderCost = orderCost.getValue() != null
                        ? orderCost.getValue()
                        : BigDecimal.ZERO;
                BigDecimal newValue = currentOrderCost
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                        .multiply(bonusPercentage) // Теперь bonusPercentage - effectively final
                        .setScale(2, RoundingMode.HALF_UP);
                accruedBonusesField.setValue(newValue);
                deductedBonusesField.setReadOnly(true);
                deductedBonusesField.setValue(BigDecimal.ZERO);
            } else {
                accruedBonusesField.setReadOnly(true);
                accruedBonusesField.setValue(BigDecimal.ZERO);
                deductedBonusesField.setReadOnly(false);
            }
        });

        // Инициализация состояния полей при открытии диалога
        String initialValue = radioGroup.getValue();
        if ("Начислить".equals(initialValue)) {
            accruedBonusesField.setReadOnly(true);
            deductedBonusesField.setReadOnly(true);
        } else {
            accruedBonusesField.setReadOnly(true);
            deductedBonusesField.setReadOnly(false);
        }

        Button addBtn = new Button("Оплатить", ignored -> {
            try {
                orderService.save(order);  // <-- Сохраняем изменения из формы

                // Проверка перед сохранением
                BigDecimal deducted = deductedBonusesField.getValue() != null
                        ? deductedBonusesField.getValue()
                        : BigDecimal.ZERO;

                BigDecimal available = totalBonusesField.getValue() != null
                        ? totalBonusesField.getValue()
                        : BigDecimal.ZERO;

                if (deducted.compareTo(available) > 0) {
                    Notification.show("Ошибка: Сумма списания превышает доступные бонусы",
                            3000, Notification.Position.MIDDLE);
                    return;
                }

                // Создание объекта InvoiceForPayment
                InvoiceForPayment invoice = new InvoiceForPayment();

                // Заполнение данных
                invoice.setTotalCost(orderCost.getValue() != null
                        ? orderCost.getValue()
                        : BigDecimal.ZERO);

                invoice.setAccruedBonuses(accruedBonusesField.getValue() != null
                        ? accruedBonusesField.getValue()
                        : BigDecimal.ZERO);

                invoice.setDeductedBonuses(deductedBonusesField.getValue() != null
                        ? deductedBonusesField.getValue()
                        : BigDecimal.ZERO);

                // Расчет итоговой стоимости
                BigDecimal discountedCost = invoice.getTotalCost().subtract(
                        invoice.getDeductedBonuses() != null
                                ? invoice.getDeductedBonuses()
                                : BigDecimal.ZERO
                );
                invoice.setDiscountedCost(discountedCost);

                // Связь с заказом
                invoice.setOrders(order);

                // Сохранение в базу
                invoiceForPaymentService.save(invoice);

                // Обновление статуса заказа
                order.setOrderStatusId(4L); // 4 - Оплачен
                orderService.save(order);

                // Уведомление и закрытие
                Notification.show("Заказ #" + order.getNumberOfOrder() + " оплачен",
                        3000, Notification.Position.TOP_CENTER);
                dialog.close();
                onCloseDialogOrderForm.run(); // Обновление интерфейса
            } catch (Exception ex) {
                Notification.show("Ошибка оплаты: " + ex.getMessage(),
                        5000, Notification.Position.TOP_CENTER);
                ex.printStackTrace();
            }
        });

        VerticalLayout layout = new VerticalLayout(
                new HorizontalLayout(orderCost, new Span(" - сумма к оплате")),
                new HorizontalLayout(totalBonusesField, new Span(" - доступно бонусов")),
                radioGroup,
                new HorizontalLayout(accruedBonusesField, bonusSpan),
                new HorizontalLayout(deductedBonusesField, new Span(" - списать бонусы")),
                new HorizontalLayout(addBtn, new Button("Отмена", ignored -> dialog.close()))
        );
        layout.setPadding(false);
        dialog.add(layout);
        dialog.open();
    }

    private void openShowInvoice() {
        List<InvoiceForPayment> invoices = invoiceForPaymentService.findByOrderId(order.getId());
        if (invoices.isEmpty()) {
            Notification.show("Чек не найден для этого заказа", 3000, Notification.Position.TOP_CENTER);
            return;
        }
        InvoiceForPayment invoice = invoices.get(invoices.size() - 1);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Чек по заказу");
        dialog.setWidth("450px");

        VerticalLayout content = new VerticalLayout();
        content.add(new Span(String.format("Чек по заказу № %s от %s",
                order.getNumberOfOrder(), order.getDateOfOrder())));
        content.add(new Span("---------------------------------------------------------"));
        content.add(new Span("Описание работ: " + order.getComment()));
        content.add(new Span("---------------------------------------------------------"));
        content.add(new Span(String.format("Стоимость работ и комплектующих: %,.2f ₽",
                invoice.getTotalCost())));
        content.add(new Span(String.format("Оплачено рублями: %,.2f ₽",
                invoice.getDiscountedCost())));
        content.add(new Span(String.format("Оплачено бонусами: %,.2f ₽",
                invoice.getDeductedBonuses())));
        content.add(new Span("---------------------------------------------------------"));
        content.add(new Span(String.format("Начислено бонусов: %,.2f ₽",
                invoice.getAccruedBonuses())));
        content.add(new Span("---------------------------------------------------------"));
        content.add(new Span(String.format("Итоговая сумма: %,.2f ₽",
                invoice.getDiscountedCost().add(invoice.getDeductedBonuses()))));

        // Контейнер для кнопки
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setPadding(true);

        Button closeBtn = new Button("Закрыть", VaadinIcon.CLOSE.create(),
                e -> dialog.close());
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        closeBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        buttonLayout.add(closeBtn);
        content.add(buttonLayout);
        content.setHorizontalComponentAlignment(Alignment.CENTER, buttonLayout);

        dialog.add(content);
        dialog.open();
    }

    private void openShowAvailableOffices(){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Где можно починить");
        dialog.setWidth("400px");

        VerticalLayout layout = new VerticalLayout(
                new Button("Закрыть", ignored -> dialog.close())
        );
        layout.setPadding(false);
        dialog.add(layout);
        dialog.open();
    }
}