package com.example.application.views.orders;

import com.example.application.data.Services;
import com.example.application.data.components.Component;
import com.example.application.data.components.ComponentService;
import com.example.application.data.orders.*;
import com.example.application.services.ServicesService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class OrderForm extends FormLayout {
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
    private TextField productField = new TextField("Товар");
    private IntegerField quantityField = new IntegerField("Количество");
    private Binder<Orders> binder = new Binder<>(Orders.class);

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

        // Кнопки управления
        Button saveBtn = createSaveButton();
        Button cancelBtn = createCancelButton();
        Button addServiceBtn = createAddServiceButton();
        Button addComponentBtn = createAddComponentButton();

        if (order.getId() == null) {
            order.setClient(currentClient);
        }

        add(new VerticalLayout(
                new FormLayout(productField, quantityField),
                new HorizontalLayout(saveBtn, cancelBtn),
                addServiceBtn,
                servicesGrid,
                addComponentBtn,
                componentsGrid
        ));

        refreshGrids();
        setSizeFull();
    }

    private void configureBinder() {
        binder.forField(productField)
                .asRequired("Введите название товара")
                .bind(Orders::getProduct, Orders::setProduct);

        binder.forField(quantityField)
                .asRequired("Введите количество")
                .withValidator(q -> q > 0, "Количество должно быть положительным")
                .bind(Orders::getQuantity, Orders::setQuantity);

        binder.readBean(order);
    }

    private void configureServicesGrid() {
        servicesGrid.removeAllColumns();

        servicesGrid.addColumn(os -> os.getServices().getServiceName())
                .setHeader("Услуга");

        servicesGrid.addColumn(os -> os.getServices().getCost())
                .setHeader("Стоимость");

        servicesGrid.addColumn(os -> os.getServices().getTimeToCompleteMinutes())
                .setHeader("Время выполнения (минуты)");

        servicesGrid.addComponentColumn(os -> {
            Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), e -> deleteService(os));
            Button editBtn = new Button("Изменить", VaadinIcon.EDIT.create(), e -> openEditDialog(os));
            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Действия");
    }

    private void configureComponentsGrid() {
        componentsGrid.removeAllColumns();

        componentsGrid.addColumn(oc -> oc.getComponent().getCategory().getTypeOfDevice().getTypeOfDeviceName())
                .setHeader("Тип устройства");

        componentsGrid.addColumn(oc -> oc.getComponent().getCategory().getTypeOfPartName())
                .setHeader("Категория компонента");

        componentsGrid.addColumn(oc -> oc.getComponent().getName())
                .setHeader("Название");

        componentsGrid.addColumn(oc -> oc.getComponent().getCost())
                .setHeader("Стоимость");

        componentsGrid.addComponentColumn(oc -> {
            Button deleteBtn = new Button("Удалить", VaadinIcon.TRASH.create(), e -> deleteComponent(oc));
            Button editBtn = new Button("Изменить", VaadinIcon.EDIT.create(), e -> openComponentEditDialog(oc));
            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Действия");
    }

    private Button createSaveButton() {
        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK.create(), e -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveBtn;
    }

    private Button createCancelButton() {
        Button cancelBtn = new Button("Отмена", VaadinIcon.CLOSE.create(), e -> onCancel.run());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return cancelBtn;
    }

    private Button createAddServiceButton() {
        Button btn = new Button("Добавить услугу", VaadinIcon.PLUS.create(), e -> openAddServiceDialog());
        btn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        return btn;
    }

    private Button createAddComponentButton() {
        Button btn = new Button("Добавить компонент", VaadinIcon.PLUS.create(), e -> openAddComponentDialog());
        btn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btn.getStyle().set("margin-top", "1em");
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

    private void refreshServicesGrid() {
        if (order.getId() != null) {
            servicesGrid.setItems(orderServicesService.findByOrderId(order.getId()));
        }
    }

    private void refreshComponentsGrid() {
        if (order.getId() != null) {
            componentsGrid.setItems(orderComponentsService.findByOrderId(order.getId()));
        }
    }

    // Методы для работы с услугами
    private void openAddServiceDialog() {
        if (order.getId() == null) {
            Notification.show("Сначала сохраните основной заказ");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setWidth("900px"); // Установка ширины

        dialog.setHeaderTitle("Добавление услуги"); // Добавляем заголовок

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
        dialog.setWidth("900px"); // Установка ширины
        dialog.setHeaderTitle("Добавление компонента"); // Добавляем заголовок

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

    // Заглушки для редактирования (реализуйте по аналогии с добавлением)
    private void openEditDialog(OrderServices os) {
        // Реализация редактирования услуги
    }

    private void openComponentEditDialog(OrderComponents oc) {
        // Реализация редактирования компонента
    }
}