package com.example.application.views.orders;

import com.example.application.data.orders.Clients;
import com.example.application.data.orders.ClientsService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class ClientForm extends FormLayout {
    private final Clients client;
    private final ClientsService clientService;
    private final Runnable refreshCallback;

    private final TextField firstName = new TextField("Имя клиента");
    private final TextField lastName = new TextField("Фамилия клиента");
    private final TextField middleName = new TextField("Отчество клиента");
    private final DatePicker dateOfBirth = new DatePicker("Дата рождения клиента");
    private final EmailField email = new EmailField("Электронная почта клиента");
    private final TextField phoneNumber = new TextField("Номер телефона клиента");
    private final ComboBox<String> gender = new ComboBox<>("Пол клиента");
    private final TextField cityOfResidence = new TextField("Город проживания клиента");

    private final Binder<Clients> binder = new Binder<>(Clients.class);

    public ClientForm(Clients client, ClientsService service, Runnable refreshCallback) {
        this.client = client;
        this.clientService = service;
        this.refreshCallback = refreshCallback;
        initForm();
    }

    private void initForm() {
        gender.setItems("м", "ж");
        gender.setPlaceholder("Выберите пол");

        // Ручная привязка вместо bindInstanceFields()
        binder.forField(firstName)
                .asRequired("Имя обязательно")
                .bind(Clients::getFirstName, Clients::setFirstName);
        binder.forField(lastName)
                .asRequired("Фамилия обязательно")
                .bind(Clients::getLastName, Clients::setLastName);
        binder.forField(middleName)
                .bind(Clients::getMiddleName, Clients::setMiddleName);
        binder.forField(dateOfBirth)
                .asRequired("Дата рождения обязательно")
                .bind(Clients::getDateOfBirth, Clients::setDateOfBirth);
        binder.forField(gender)
                .asRequired("Пол обязателен")
                .bind(Clients::getGender, Clients::setGender);
        binder.forField(email)
                .asRequired("Электронная почта обязательно")
                .bind(Clients::getEmail, Clients::setEmail);
        binder.forField(phoneNumber)
                .asRequired("Номер телефона обязательно")
                .bind(Clients::getPhone, Clients::setPhone);
        binder.forField(cityOfResidence)
                .asRequired("Город проживания обязателен")
                .bind(Clients::getCityOfResidence, Clients::setCityOfResidence);

        binder.readBean(client); // Загрузка данных

        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create(), ignored -> save());
        styleButton(saveBtn, "primary");

        Button cancelBtn = new Button("Отмена", VaadinIcon.CLOSE_CIRCLE_O.create(), ignored -> this.setVisible(false));
        styleButton(cancelBtn, "error");

        add(firstName, lastName, middleName, dateOfBirth, gender, email, phoneNumber, cityOfResidence,
                new HorizontalLayout(saveBtn, cancelBtn) {{
                    setWidthFull();
                    setJustifyContentMode(JustifyContentMode.END);
                }}
                );
    }

    private void save() {
        if (binder.writeBeanIfValid(client)) {
            clientService.save(client);
            refreshCallback.run();
            this.setVisible(false);
        }
    }
    private void styleButton(Button button, String theme) {
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        if ("primary".equals(theme)) {
            button.getStyle().set("color", "var(--lumo-primary-text-color)");
        } else {
            button.getStyle().set("color", "var(--lumo-error-text-color)");
        }
        button.getStyle().set("margin-right", "1em");
    }
}