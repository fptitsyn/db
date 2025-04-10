package com.example.application.views.orders;

import com.example.application.data.services.Services;
import com.example.application.data.components.Component;
import com.example.application.data.components.ComponentService;
import com.example.application.data.orders.*;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;

import java.math.BigDecimal;

public class OrderForm extends VerticalLayout {
    private final Orders order;
    private final Clients currentClient;
    private final OrdersService orderService;
    private final OrderServicesService orderServicesService;
    private final ServicesService servicesService;
    private final OrderComponentsService orderComponentsService;
    private final ComponentService componentService;
    private final Runnable onSave;
    private final Runnable onCancel;

    // Основные поля формы
    BigDecimalField orderCost = new BigDecimalField();
    private TextArea commentField = new TextArea ("Комментарий к заказу");

    private Binder<Orders> binder = new Binder<>(Orders.class);
    // Добавляем переменные для колонок (суммы итого)
    private Grid.Column<OrderServices> costColumn;
    private Grid.Column<OrderServices> timeColumn;
    private Grid.Column<OrderComponents> costComponentsColumn;
    private BigDecimal totalServicesCost = BigDecimal.ZERO;
    private BigDecimal totalComponentsCost = BigDecimal.ZERO;

    // Grid для услуг
    private Grid<OrderServices> servicesGrid = new Grid<>(OrderServices.class);
    // Grid для компонентов
    private Grid<OrderComponents> componentsGrid = new Grid<>(OrderComponents.class);

    public OrderForm(Orders order,
                     Clients currentClient,
                     OrdersService orderService,
                     OrderServicesService orderServicesService,
                     ServicesService servicesService,
                     OrderComponentsService orderComponentsService,
                     ComponentService componentService,
                     Runnable onSave,
                     Runnable onCancel) {
        this.order = order;
        this.currentClient = currentClient;
        this.orderService = orderService;
        this.orderServicesService = orderServicesService;
        this.servicesService = servicesService;
        this.orderComponentsService = orderComponentsService;
        this.componentService = componentService;
        this.onSave = onSave;
        this.onCancel = onCancel;

        initForm();
    }

    private void initForm() {
        configureBinder();
        configureServicesGrid();
        configureComponentsGrid();

        if (order.getId() == null)
        {
            order.setClient(currentClient);
        }
        else {
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
                        createAddServiceButton(),createAddComponentButton(),
                        createSetWorkOrderButton(), createPayButton(), createSelectLocationButton(), createCancelOrderButton()),
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
            Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), e -> deleteService(os));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteBtn.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            return deleteBtn;
        }).setHeader("Действия");
        // Обновляем футеры при изменении данных
        servicesGrid.getDataProvider().addDataProviderListener(event -> updateFooters());
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
            Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), e -> deleteComponent(oc));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteBtn.getStyle()
                    .set("margin-right", "1em")
                    .set("color", "var(--lumo-primary-text-color)");
            return deleteBtn;
        }).setHeader("Действия");

        // Обновляем футеры при изменении данных
        componentsGrid.getDataProvider().addDataProviderListener(event -> updateComponentsFooters());
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
        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK.create(), e -> save());

        // Устанавливаем видимость кнопки
        //saveBtn.setVisible(showForNewOrder());

        saveBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return saveBtn;
    }

    private Button createCancelButton() {
        Button cancelBtn = new Button("Закрыть", VaadinIcon.CLOSE.create(), e -> onCancel.run());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return cancelBtn;
    }

    private Button createAddServiceButton() {
        Button btn = new Button("Добавить услугу", VaadinIcon.PLUS.create(), e -> openAddServiceDialog());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createAddComponentButton() {
        Button btn = new Button("Добавить компонент", VaadinIcon.PLUS.create(), e -> openAddComponentDialog());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createSetWorkOrderButton() {
        Button btn = new Button("Передать в работу", VaadinIcon.TOOLS.create(), e -> {
            order.setOrderStatusId(2L); // 4 - ID статуса "Распределен"
            orderService.save(order);
            Notification.show("Заказ #" + order.getNumberOfOrder() + " распределен",
                    3000, Notification.Position.TOP_CENTER);
            onCancel.run();
        });

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }
    private Button createPayButton() {
        Button btn = new Button("Оплатить заказ", VaadinIcon.MONEY.create(), e -> {
            order.setOrderStatusId(4L); // 4 - ID статуса "Оплачен"
            orderService.save(order);
            Notification.show("Заказ #" + order.getNumberOfOrder() + " оплачен",
                    3000, Notification.Position.TOP_CENTER);
            onCancel.run();
        });

        // Устанавливаем видимость кнопки
        btn.setVisible(showForPayOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }
    private Button createCancelOrderButton() {
        Button btn = new Button("Отменить заказ", VaadinIcon.FILE_REMOVE.create(), e -> {
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

    private boolean showForNewOrder() {
        // Проверяем что заказ сохранен и статус = 1 ('Создан')
        return order.getId() != null
                && order.getOrderStatus() != null
                && order.getOrderStatus().getId().equals(1L);
    }

    private boolean showForPayOrder() {
        // Проверяем что заказ сохранен и статус = 1 ('Создан')
        return order.getId() != null
                && order.getOrderStatus() != null
                && order.getOrderStatus().getId().equals(3L);
    }

    private void showCancelConfirmationDialog() {
        ConfirmDialog confirmDialog = new ConfirmDialog(
                "Подтверждение отмены",
                "Вы уверены, что хотите отменить заказ #" + order.getNumberOfOrder() + "?",
                "Подтвердить отмену", confirmEvent -> {
            try {
                order.setOrderStatusId(5L); // 5 - ID статуса "Отменен"
                orderService.save(order);
                Notification.show("Заказ #" + order.getNumberOfOrder() + " отменен",
                        3000, Notification.Position.TOP_CENTER);
                onCancel.run();
            } catch (Exception ex) {
                Notification.show("Ошибка отмены заказа: " + ex.getMessage(),
                        5000, Notification.Position.TOP_CENTER);
            }
        },
                "Отмена", cancelEvent -> {}
        );

        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.open();
    }

    private Button createSelectLocationButton() {
        Button btn = new Button("Где починить?", VaadinIcon.QUESTION_CIRCLE_O.create(), e -> openAddComponentDialog());

        // Устанавливаем видимость кнопки
        btn.setVisible(showForNewOrder());

        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private void save() {
        if (binder.writeBeanIfValid(order)) {
            try {
                // Устанавливаем итоговую стоимость в заказ
                order.setTotalCost(orderCost.getValue());
                orderService.save(order);
                refreshGrids();
                onSave.run();
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

        Button addBtn = new Button("Добавить", e -> {
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
                new HorizontalLayout(addBtn, new Button("Отмена", ev -> dialog.close()))
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

        Button addBtn = new Button("Добавить", e -> {
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
                new HorizontalLayout(addBtn, new Button("Отмена", ev -> dialog.close()))
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

}