package com.example.application.views.orders;

import com.example.application.data.Clients;
import com.example.application.services.ClientsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Route(value = "all-clients-view")
@PageTitle("Список клиентов")
@Menu(order = 11, icon = LineAwesomeIconUrl.ADDRESS_BOOK_SOLID)

@RolesAllowed({"SALES","GOD"})
public class ClientsView extends VerticalLayout {
    private final ClientsService clientService;
    private final Grid<Clients> clientGrid = new Grid<>(Clients.class, false);
    private GridListDataView<Clients> dataView;
    private ClientFilter clientFilter;

    public ClientsView(ClientsService clientService) {
        this.clientService = clientService;
        initView();
    }

    private void initView() {
        configureGrid();
        configureFilter();
        addFiltersToHeader();

        Button addClientBtn = new Button("Новый", VaadinIcon.PLUS_SQUARE_O.create(),
                e -> showClientForm(new Clients()));

        add(addClientBtn, clientGrid);
    }

    private void configureGrid() {
        // Добавление столбцов
        Grid.Column<Clients> nameCol = clientGrid.addColumn(Clients::getFullName)
                .setHeader("ФИО")
                .setKey("name")
                .setSortable(true)
                .setWidth("120px");

        Grid.Column<Clients> dobCol = clientGrid.addColumn(Clients::getDateOfBirth)
                .setHeader("Дата рождения")
                .setSortable(true)
                .setWidth("40px");

        Grid.Column<Clients> genderCol = clientGrid.addColumn(Clients::getGender)
                .setHeader("Пол")
                .setWidth("3px");

        Grid.Column<Clients> emailCol = clientGrid.addColumn(Clients::getEmail)
                .setHeader("Почта")
                .setWidth("40px");

        Grid.Column<Clients> cityCol = clientGrid.addColumn(Clients::getCityOfResidence)
                .setHeader("Проживает")
                .setWidth("40px");

        clientGrid.addComponentColumn(this::createActions)
                .setHeader("Действия")
                .setWidth("420px");

        // Загрузка данных
        List<Clients> clients = clientService.findAll();
        dataView = clientGrid.setItems(clients);
    }

    private void configureFilter() {
        clientFilter = new ClientFilter(dataView);
    }

    private void addFiltersToHeader() {
        HeaderRow filterRow = clientGrid.appendHeaderRow();

        // Фильтр для ФИО
        filterRow.getCell(clientGrid.getColumnByKey("name"))
                .setComponent(createFilterHeader("ФИО", clientFilter::setFullName));

        // Фильтр для Даты рождения
        Component dobFilter = createDateFilter(clientFilter::setDateOfBirth);
        filterRow.getCell(clientGrid.getColumns().get(1)).setComponent(dobFilter);

        // Фильтр для Пола (ComboBox)
        Component genderFilter = createGenderFilter();
        filterRow.getCell(clientGrid.getColumns().get(2)).setComponent(genderFilter);

        // Фильтр для Почты
        filterRow.getCell(clientGrid.getColumns().get(3))
                .setComponent(createFilterHeader("Почта", clientFilter::setEmail));

        // Фильтр для Города
        filterRow.getCell(clientGrid.getColumns().get(4))
                .setComponent(createFilterHeader("Город", clientFilter::setCity));
    }

    private Component createFilterHeader(String label, Consumer<String> filterConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder(label);
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.addValueChangeListener(e -> filterConsumer.accept(e.getValue()));

        // Оптимизация: задержка для частых изменений
        textField.setValueChangeMode(ValueChangeMode.LAZY);
        textField.setValueChangeTimeout(500);

        return textField;
    }

    private Component createGenderFilter() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPlaceholder("Пол");
        comboBox.setItems("м", "ж");
        comboBox.setClearButtonVisible(true);
        comboBox.addValueChangeListener(e -> clientFilter.setGender(e.getValue()));
        return comboBox;
    }

    private Component createDateFilter(Consumer<String> filterConsumer) {
        DatePicker datePicker = new DatePicker();
        datePicker.setPlaceholder("Дата рождения");
        datePicker.setClearButtonVisible(true);
        datePicker.addValueChangeListener(e ->
                filterConsumer.accept(e.getValue() != null ? e.getValue().toString() : null)
        );
        return datePicker;
    }

    private class ClientFilter {
        private final GridListDataView<Clients> dataView;

        private String fullName;
        private String dateOfBirth;
        private String gender;
        private String email;
        private String city;

        public ClientFilter(GridListDataView<Clients> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
            dataView.refreshAll();
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            dataView.refreshAll();
        }

        public void setGender(String gender) {
            this.gender = gender;
            dataView.refreshAll();
        }

        public void setEmail(String email) {
            this.email = email;
            dataView.refreshAll();
        }

        public void setCity(String city) {
            this.city = city;
            dataView.refreshAll();
        }

        public boolean test(Clients client) {
            boolean matchesFullName = matches(client.getFullName(), fullName);
            boolean matchesDob = matchesDate(client.getDateOfBirth(), dateOfBirth);
            boolean matchesGender = matches(client.getGender(), gender);
            boolean matchesEmail = matches(client.getEmail(), email);
            boolean matchesCity = matches(client.getCityOfResidence(), city);

            return matchesFullName && matchesDob && matchesGender && matchesEmail && matchesCity;
        }

        private boolean matchesDate(LocalDate date, String searchTerm) {
            if (searchTerm == null || searchTerm.isEmpty()) return true;
            if (date == null) return false;

            return date.toString().contains(searchTerm);
        }

        private boolean matches(String value, String searchTerm) {
            if (searchTerm == null || searchTerm.isEmpty()) return true;
            if (value == null) return false;

            return value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }


    private HorizontalLayout createActions(Clients client) {
        Button editBtn = new Button("Изменить", VaadinIcon.EDIT.create(), e -> showClientForm(client));
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
            ui.navigate(BonusForm.class, parameters);
        });
    }
    public void updateGrid() {
        List<Clients> clients = clientService.findAll();
        dataView = clientGrid.setItems(clients);
        configureFilter(); // Пересоздаем фильтр для новых данных
    }
}