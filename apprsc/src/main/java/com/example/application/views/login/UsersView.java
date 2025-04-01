package com.example.application.views.login;

import com.example.application.data.Employees;
import com.example.application.data.Users;
import com.example.application.data.Role;
import com.example.application.services.EmployeesService;
import com.example.application.services.UsersService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Пользователи")
@Route("users-detail/:entityID?/:action?(edit)")
@Menu(order = 50, icon = LineAwesomeIconUrl.USER_COG_SOLID)
@RolesAllowed({"ADMIN","GOD"})
public class UsersView extends Div implements BeforeEnterObserver {

    private final String ENTITY_ID = "entityID";
    private final String GRID_DB_OBJECT_EDIT_ROUTE_TEMPLATE = "users-detail/%s/edit";

    private final Grid<Users> grid = new Grid<>(Users.class, false);
    private TextField username;
    private PasswordField hashedPassword;
    private TextField name;
    private ComboBox<Employees> employeeComboBox;

    private final Button cancel = new Button("Очистить", VaadinIcon.CLOSE_CIRCLE_O.create());
    private final Button save = new Button("Сохранить", VaadinIcon.CHECK_SQUARE_O.create());

    private BeanValidationBinder<Users> binder;
    private Users DBEntity;
    private final UsersService usersService;
    private final EmployeesService employeesService;
    private final PasswordEncoder passwordEncoder;
    private CheckboxGroup<Role> rolesCheckbox;

    public UsersView(UsersService usersService,
                     PasswordEncoder passwordEncoder,
                     EmployeesService employeesService) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.employeesService = employeesService;
        addClassNames("user-view");

        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        configureGrid();
        setupBinder();
        setupSaveHandler();
    }

    private void configureGrid() {
        grid.removeAllColumns();

        // Колонка "Сотрудник" с сортировкой
        Grid.Column<Users> employeeColumn = grid.addColumn(user -> user.getEmployee() != null
                        ? user.getEmployee().getFullName()
                        : "")
                .setHeader("Сотрудник")
                .setKey("employee")
                .setAutoWidth(true)
                .setSortable(true)
                .setComparator(u -> {
                    Employees e = u.getEmployee();
                    return e != null ? e.getFullName() : "";
                });

        grid.addColumn("username").setAutoWidth(true).setHeader("Пользователь");
        grid.addColumn("name").setAutoWidth(true).setHeader("Ник");

        grid.addColumn(user -> user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.joining(", "))).setHeader("Роли").setAutoWidth(true);

        // Настройка сортировки по умолчанию
        List<GridSortOrder<Users>> sortOrder = Collections.singletonList(
                new GridSortOrder<>(employeeColumn, SortDirection.ASCENDING)
        );
        grid.sort(sortOrder);

        grid.setItems(query -> usersService.listWithEmployees(
                VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(GRID_DB_OBJECT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(UsersView.class);
            }
        });
    }

    private void setupBinder() {
        binder = new BeanValidationBinder<>(Users.class);
        binder.forField(username).bind(Users::getUsername, Users::setUsername);
        binder.forField(name).bind(Users::getName, Users::setName);
        binder.forField(rolesCheckbox)
                .asRequired("Необходимо выбрать минимум одну роль")
                .bind(Users::getRoles, Users::setRoles);
        binder.forField(employeeComboBox)
                .bind(Users::getEmployee, Users::setEmployee);
    }

    private void setupSaveHandler() {
        save.addClickListener(e -> {
            try {
                if (this.DBEntity == null) {
                    this.DBEntity = new Users();
                }
                binder.writeBean(this.DBEntity);

                handlePasswordEncoding();
                usersService.save(this.DBEntity);

                clearForm();
                refreshGrid();
                refreshEmployeeComboBox();
                Notification.show("Данные обновлены");
                UI.getCurrent().navigate(UsersView.class);

            } catch (ObjectOptimisticLockingFailureException ex) {
                handleOptimisticLockException();
            } catch (ValidationException ex) {
                Notification.show("Проверьте правильность введенных данных");
            }
        });
    }

    private void handlePasswordEncoding() throws ValidationException {
        String rawPassword = hashedPassword.getValue();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            DBEntity.setHashedPassword(passwordEncoder.encode(rawPassword));
        } else if (DBEntity.getId() == null) {
            Notification.show("Пароль обязателен для нового пользователя");
            // Исправление 2: правильный конструктор исключения
            throw new ValidationException(
                    Collections.emptyList(),
                    Collections.emptyList()
            );
        }
    }

    private void handleOptimisticLockException() {
        Notification n = Notification.show(
                "Ошибка обновления: данные были изменены другим пользователем");
        n.setPosition(Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        FormLayout formLayout = new FormLayout();
        createFormFields(formLayout);
        editorLayoutDiv.add(formLayout);

        createButtonLayout(editorLayoutDiv);
        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createFormFields(FormLayout formLayout) {
        username = new TextField("Логин");
        hashedPassword = new PasswordField("Пароль");
        name = new TextField("Отображаемое имя");

        employeeComboBox = new ComboBox<>("Сотрудник");
        employeeComboBox.setItemLabelGenerator(Employees::getFullName);
        employeeComboBox.setPlaceholder("Выберите сотрудника...");
        configureEmployeeComboBox();

        rolesCheckbox = new CheckboxGroup<>("Роли");
        rolesCheckbox.setItems(Role.values());
        rolesCheckbox.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        formLayout.add(username, hashedPassword, name, employeeComboBox, rolesCheckbox);
    }

    private void configureEmployeeComboBox() {
        employeeComboBox.setItems(query -> {
            String filter = query.getFilter().orElse("").toLowerCase();
            List<Employees> allEmployees = employeesService.findAll(filter);

            Employees currentEmployee = DBEntity != null ? DBEntity.getEmployee() : null;

            return allEmployees.stream()
                    .filter(e -> e.equals(currentEmployee) || !usersService.isEmployeeLinked(e))
                    .skip(query.getOffset())
                    .limit(query.getLimit());
        });
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        save.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> entityID = event.getRouteParameters().get(ENTITY_ID).map(Long::parseLong);
        if (entityID.isPresent()) {
            Optional<Users> DBEntityFromBackend = usersService.get(entityID.get());
            if (DBEntityFromBackend.isPresent()) {
                populateForm(DBEntityFromBackend.get());
            } else {
                Notification.show("Запись не найдена", 3000, Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(UsersView.class);
            }
        }
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void refreshEmployeeComboBox() {
        employeeComboBox.getDataProvider().refreshAll();
    }

    private void populateForm(Users value) {
        this.DBEntity = value;
        if (value != null) {
            username.setValue(value.getUsername());
            name.setValue(value.getName());
            hashedPassword.setValue("");
            rolesCheckbox.setValue(value.getRoles());
            employeeComboBox.setValue(value.getEmployee());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        username.setValue("");
        name.setValue("");
        hashedPassword.setValue("");
        rolesCheckbox.setValue(Collections.emptySet());
        employeeComboBox.clear();
    }

    private void clearForm() {
        populateForm(null);
        refreshEmployeeComboBox();
    }
}