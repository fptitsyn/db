package com.example.application.views.orders;

import com.example.application.data.Services;
import com.example.application.data.components.Component;
import com.example.application.data.components.ComponentService;
import com.example.application.data.orders.*;
import com.example.application.services.ServicesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

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
    private Span orderNumber = new Span();
    private Span orderDate = new Span();
    private Span orderStatus = new Span();

    private TextArea commentField = new TextArea ("Комментарий к заказу");
    private Binder<Orders> binder = new Binder<>(Orders.class);
    // Добавляем переменные для колонок (суммы итого)
    private Grid.Column<OrderServices> costColumn;
    private Grid.Column<OrderServices> timeColumn;
    private Grid.Column<OrderComponents> costComponentsColumn;

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

        if (order.getId() == null) {
            order.setClient(currentClient);
        }

        //Как всегда временно
        orderNumber.setText("Номер заказа: " + order.getNumberOfOrder());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = order.getDateOfOrder().format(formatter);
        orderDate.setText("Дата создания: " + formattedDate);

        orderStatus.setText("Статус заказа: " + order.getOrderStatus());

        commentField.setWidthFull();

        refreshGrids();

        add(new HorizontalLayout(orderNumber, orderDate, orderStatus),
                commentField,
                servicesGrid,
                componentsGrid,
        new HorizontalLayout(createSaveButton(), createCancelButton(),
                createAddServiceButton(),createAddComponentButton(),
                createSetWorkOrderButton(), createSelectLocationButton())
        );



        setSizeFull(); // Для всей формы OrderForm
    }

    private void configureBinder() {
        binder.forField(commentField)
                .asRequired("Введите комментарий к заказу")
                .bind(Orders::getComment, Orders::setComment);


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
        timeColumn = servicesGrid.addColumn(os -> os.getServices().getTimeToCompleteMinutes())
                .setHeader("Время выполнения (минуты)").setTextAlign(ColumnTextAlign.END);
        timeColumn.setFooter("Итого: 0 мин");


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
        double totalCost = servicesGrid.getDataProvider().fetch(new Query<>())
                .mapToDouble(os -> os.getServices().getCost())
                .sum();

        int totalTime = servicesGrid.getDataProvider().fetch(new Query<>())
                .mapToInt(os -> os.getServices().getTimeToCompleteMinutes())
                .sum();

        costColumn.setFooter(String.format("Итого: %,.2f ₽", totalCost));
        timeColumn.setFooter(String.format("Итого: %,d мин", totalTime));
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
        // Для BigDecimal используем правильное суммирование
        BigDecimal totalComponentsCost = componentsGrid.getDataProvider().fetch(new Query<>())
                .map(os -> os.getComponent().getCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        costComponentsColumn.setFooter(String.format("Итого: %,.2f ₽", totalComponentsCost));
    }

    private void refreshComponentsGrid() {
        if (order.getId() != null) {
            componentsGrid.setItems(orderComponentsService.findByOrderId(order.getId()));
            updateComponentsFooters(); // Добавляем вызов обновления
        }
    }

    private Button createSaveButton() {
        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK.create(), e -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return saveBtn;
    }

    private Button createCancelButton() {
        Button cancelBtn = new Button("Отмена", VaadinIcon.CLOSE.create(), e -> onCancel.run());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return cancelBtn;
    }

    private Button createAddServiceButton() {
        Button btn = new Button("Добавить услугу", VaadinIcon.PLUS.create(), e -> openAddServiceDialog());
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createAddComponentButton() {
        Button btn = new Button("Добавить компонент", VaadinIcon.PLUS.create(), e -> openAddComponentDialog());
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createSetWorkOrderButton() {
        Button btn = new Button("Передать в работу", VaadinIcon.TOOLS.create(), e -> openAddComponentDialog());
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private Button createSelectLocationButton() {
        Button btn = new Button("Где починить?", VaadinIcon.TOOLS.create(), e -> openAddComponentDialog());
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        return btn;
    }

    private void save() {
        if (binder.writeBeanIfValid(order)) {
            orderService.save(order);
            refreshGrids();
            onSave.run();
        }
    }

    private void refreshGrids() {
        refreshServicesGrid();
        refreshComponentsGrid();
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
        refreshComponentsGrid();
    }

}