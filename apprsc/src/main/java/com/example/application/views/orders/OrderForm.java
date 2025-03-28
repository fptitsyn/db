package com.example.application.views.orders;

import com.example.application.data.Clients;
import com.example.application.data.Orders;
import com.example.application.services.OrdersService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class OrderForm extends FormLayout {
    private final Orders order;
    private final Clients currentClient;
    private final OrdersService orderService;
    private final Runnable refreshCallback;

    // Поля формы
    private TextField productField = new TextField("Товар");
    private IntegerField quantityField = new IntegerField("Количество");
    private Binder<Orders> binder = new Binder<>(Orders.class);

    // Исправленный конструктор
    public OrderForm(Orders order, Clients currentClient, OrdersService orderService, Runnable refreshCallback) {
        this.order = order;
        this.currentClient = currentClient;
        this.orderService = orderService;
        this.refreshCallback = refreshCallback;
        initForm();
    }

    private void initForm() {
        // Настройка биндера
        binder.forField(productField)
                .asRequired("Введите название товара")
                .bind(Orders::getProduct, Orders::setProduct);

        binder.forField(quantityField)
                .asRequired("Введите количество")
                .withValidator(q -> q > 0, "Количество должно быть положительным")
                .bind(Orders::getQuantity, Orders::setQuantity);

        // Если заказ новый, устанавливаем клиента
        if (order.getId() == null) {
            order.setClient(currentClient);
        }

        binder.readBean(order);

        // Кнопки
        Button saveBtn = new Button("Сохранить", e -> save());
        Button cancelBtn = new Button("Отмена", e -> this.setVisible(false));

        add(new FormLayout(productField, quantityField), new HorizontalLayout(saveBtn, cancelBtn));
    }

    private void save() {
        if (binder.writeBeanIfValid(order)) {
            orderService.save(order);
            refreshCallback.run();
            this.setVisible(false);
        }
    }
}
