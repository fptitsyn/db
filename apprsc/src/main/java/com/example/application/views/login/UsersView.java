package com.example.application.views.login;

import com.example.application.data.Users;
import com.example.application.services.UsersService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Collections;
import java.util.Optional;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.example.application.data.Role;
import java.util.stream.Collectors;

@PageTitle("Пользователи")
@Route("users-detail/:entityID?/:action?(edit)")
@Menu(order = 50, icon = LineAwesomeIconUrl.COLUMNS_SOLID)

@RolesAllowed({"ADMIN","GOD"})
public class UsersView extends Div implements BeforeEnterObserver {

    private final String ENTITY_ID = "entityID";
    private final String GRID_DB_OBJECT_EDIT_ROUTE_TEMPLATE = "users-detail/%s/edit";

    private final Grid<Users> grid = new Grid<>(Users.class, false);
    private TextField username;
    private PasswordField hashedPassword;
    private TextField name;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<Users> binder;
    private Users DBEntity;
    private final UsersService DBService;
    private final PasswordEncoder passwordEncoder;
    private CheckboxGroup<Role> rolesCheckbox;

    public UsersView(UsersService DBService, PasswordEncoder passwordEncoder) {
        this.DBService = DBService;
        this.passwordEncoder = passwordEncoder;
        addClassNames("user-view");

        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn(user -> user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.joining(", ")))
                .setHeader("Роли")
                .setAutoWidth(true);
        grid.setItems(query -> DBService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(GRID_DB_OBJECT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(UsersView.class);
            }
        });

        binder = new BeanValidationBinder<>(Users.class);
        binder.forField(username).bind(Users::getUsername, Users::setUsername);
        binder.forField(name).bind(Users::getName, Users::setName);
        binder.forField(rolesCheckbox)
                .asRequired("Необходимо выбрать минимум одну роль")
                .bind(Users::getRoles, Users::setRoles);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.DBEntity == null) {
                    this.DBEntity = new Users();
                }
                binder.writeBean(this.DBEntity);
                String rawPassword = hashedPassword.getValue();
                if (rawPassword != null && !rawPassword.isEmpty()) {
                    DBEntity.setHashedPassword(passwordEncoder.encode(rawPassword));
                } else if (DBEntity.getId() == null) {
                    Notification.show("Пароль обязателен для нового пользователя");
                    return;
                }

                DBService.save(this.DBEntity);
                clearForm();
                refreshGrid();
                Notification.show("Данные обновлены");
                UI.getCurrent().navigate(UsersView.class);
            } catch (ObjectOptimisticLockingFailureException ex) {
                Notification n = Notification.show(
                        "Ошибка обновления: данные были изменены другим пользователем");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException ex) {
                Notification.show("Проверьте правильность введенных данных");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> entityID = event.getRouteParameters().get(ENTITY_ID).map(Long::parseLong);
        if (entityID.isPresent()) {
            Optional<Users> DBEntityFromBackend = DBService.get(entityID.get());
            if (DBEntityFromBackend.isPresent()) {
                populateForm(DBEntityFromBackend.get());
            } else {
                Notification.show("Запись не найдена", 3000, Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(UsersView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        FormLayout formLayout = new FormLayout();
        username = new TextField("Пользователь");
        hashedPassword = new PasswordField("Пароль");
        name = new TextField("Имя");
        formLayout.add(username, hashedPassword, name);

        rolesCheckbox = new CheckboxGroup<>("Роли");
        rolesCheckbox.setItems(Role.values());
        rolesCheckbox.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        formLayout.add(rolesCheckbox);

        editorLayoutDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);
        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
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

    private void populateForm(Users value) {
        this.DBEntity = value;
        if (value != null) {
            username.setValue(value.getUsername());
            name.setValue(value.getName());
            hashedPassword.setValue("");
            rolesCheckbox.setValue(value.getRoles());
        } else {
            username.setValue("");
            name.setValue("");
            hashedPassword.setValue("");
            rolesCheckbox.setValue(Collections.emptySet());
        }
    }
}