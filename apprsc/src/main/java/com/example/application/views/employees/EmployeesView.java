package com.example.application.views.employees;

import com.example.application.data.Employees;
import com.example.application.data.Services;
import com.example.application.services.EmployeesService;
import com.example.application.services.ServicesService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import net.datafaker.Faker;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Сотрудники")
@Route("employees-detail/:samplePersonID?/:action?(edit)")

@Menu(order = 40, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed({"HR","GOD"})

public class EmployeesView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "employees-detail/%s/edit";

    private final Grid<Employees> grid = new Grid<>(Employees.class, false);

    private MultiSelectComboBox<Services> servicesComboBox = new MultiSelectComboBox<>("Services");

    private TextField firstName;
    private TextField lastName;
    private TextField middleName;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private TextField comment;

    private final Button cancel = new Button("Cancel");

    private final Button save = new Button("Save");

    private final Button autoFillButton = new Button("Автозаполнить", VaadinIcon.MAGIC.create());

    private final BeanValidationBinder<Employees> binder;

    private Employees samplePerson;

    private final EmployeesService employeesService;

    private final ServicesService servicesService;

    public EmployeesView(EmployeesService employeesService, ServicesService servicesService) {
        this.employeesService = employeesService;
        this.servicesService = servicesService;
        addClassNames("employees-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        configureGrid();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(com.example.application.views.employees.EmployeesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Employees.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        autoFillButton.addClickListener(event -> autoFillWithCustomData());

        save.addClickListener(e -> {
            try {
                if (this.samplePerson == null) {
                    this.samplePerson = new Employees();
                }
                binder.writeBean(this.samplePerson);

                // Обновляем связи с услугами
                this.samplePerson.getServices().clear();
                this.samplePerson.getServices().addAll(servicesComboBox.getSelectedItems());

                employeesService.save(this.samplePerson);
                clearForm();
                refreshGrid();
                Notification.show("Данные обновлены");
                UI.getCurrent().navigate(EmployeesView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification.show("Ошибка: данные были изменены другим пользователем", 3000, Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Ошибка валидации. Проверьте введенные данные", 3000, Position.MIDDLE);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> samplePersonId = event.getRouteParameters().get(SAMPLEPERSON_ID).map(Long::parseLong);
        if (samplePersonId.isPresent()) {
            Optional<Employees> samplePersonFromBackend = employeesService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %s", samplePersonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(com.example.application.views.employees.EmployeesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        servicesComboBox.setItems(employeesService.findAllServices());
        servicesComboBox.setItemLabelGenerator(Services::getServiceName);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        middleName = new TextField("Отчество");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        phone = new TextField("Phone");
        dateOfBirth = new DatePicker("Date Of Birth");
        comment = new TextField("Комментарий");


        formLayout.add(firstName, middleName, lastName, email, phone, dateOfBirth, servicesComboBox, comment);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void autoFillWithCustomData() {
        try {
            Faker faker = new Faker(new Locale("ru"));

            firstName.setValue(faker.name().firstName());
            lastName.setValue(faker.name().lastName());
            middleName.setValue(faker.name().firstName() + "ович");
            email.setValue(faker.internet().emailAddress());
            phone.setValue("+7" + faker.phoneNumber().subscriberNumber(10));
            comment.setValue(faker.lorem().sentence(3));
            dateOfBirth.setValue(LocalDate.now()
                    .minusYears(faker.random().nextInt(18, 65))
                    .minusMonths(faker.random().nextInt(0, 12))
                    .minusDays(faker.random().nextInt(0, 28)));

        } catch (Exception e) {
            Notification.show("Ошибка автозаполнения: " + e.getMessage(),
                    3000, Position.MIDDLE);
        }
    }
    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        autoFillButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel, autoFillButton);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Employees value) {
        this.samplePerson = value;
        binder.readBean(this.samplePerson);

        // Обновляем выбранные услуги в комбобоксе
        if (value != null) {
            servicesComboBox.clear();
            servicesComboBox.select(value.getServices());
        } else {
            servicesComboBox.clear();
        }
    }

    private void configureGrid(){
        grid.removeAllColumns();
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("middleName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("phone").setAutoWidth(true);
        grid.addColumn("dateOfBirth").setAutoWidth(true);
        grid.addColumn(e -> e.getServices().stream()
                .map(Services::getServiceName)
                .collect(Collectors.joining(", "))).setHeader("Услуги");
        grid.addColumn("comment").setAutoWidth(true);

        grid.setItems(query -> employeesService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }
}
