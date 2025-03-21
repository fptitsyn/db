package com.example.application.views.orders;

import com.example.application.data.Clients;
import com.example.application.services.ClientsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

//@RolesAllowed({"SALES","GOD"})
public class ClientForm extends FormLayout {
    private final Clients client;
    private final ClientsService clientService;
    private final Runnable refreshCallback;

    // Поля формы должны быть объявлены как instance-переменные
    private TextField nameField = new TextField("Имя клиента");
    private Binder<Clients> binder = new Binder<>(Clients.class);

    public ClientForm(Clients client, ClientsService service, Runnable refreshCallback) {
        this.client = client;
        this.clientService = service;
        this.refreshCallback = refreshCallback;
        initForm();
    }

    private void initForm() {
        // Ручная привязка вместо bindInstanceFields()
        binder.forField(nameField)
                .asRequired("Имя обязательно")
                .bind(Clients::getName, Clients::setName);

        // Если есть другие поля, добавьте их здесь

        binder.readBean(client); // Загрузка данных

        Button saveBtn = new Button("Сохранить", e -> save());
        Button cancelBtn = new Button("Отмена", e -> this.setVisible(false));

        add(nameField, new HorizontalLayout(saveBtn, cancelBtn));
    }

    private void save() {
        if (binder.writeBeanIfValid(client)) {
            clientService.save(client);
            refreshCallback.run();
            this.setVisible(false);
        }
    }
}