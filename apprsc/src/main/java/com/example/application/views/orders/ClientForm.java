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

//@RolesAllowed({"SALES","GOD"})
public class ClientForm extends FormLayout {
    private final Clients client;
    private final ClientsService clientService;
    private final Runnable refreshCallback;

    // Поля формы должны быть объявлены как instance-переменные
    private TextField firstName = new TextField("Имя клиента");
    private TextField lastName = new TextField("Фамилия клиента");
    private TextField middleName = new TextField("Отчество клиента");
    private DatePicker dateOfBirth = new DatePicker("Дата рождения клиента");
    private EmailField email = new EmailField("Электронная почта клиента");
    private TextField phoneNumber = new TextField("Номер телефона клиента");
    private ComboBox<String> gender = new ComboBox<>("Пол клиента");
    private TextField cityOfResidence = new TextField("Город проживания клиента");

    private Binder<Clients> binder = new Binder<>(Clients.class);

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

        Button saveBtn = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create(), e -> save());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        Button cancelBtn = new Button("Отмена", VaadinIcon.CLOSE_CIRCLE_O.create(), e -> this.setVisible(false));
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        add(firstName, lastName, middleName, dateOfBirth, gender, email, phoneNumber, cityOfResidence, new HorizontalLayout(saveBtn, cancelBtn));
    }

    private void save() {
        if (binder.writeBeanIfValid(client)) {
            clientService.save(client);
            refreshCallback.run();
            this.setVisible(false);
        }
    }
}