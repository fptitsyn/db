package com.example.application.views.orders;

import com.example.application.data.components.ComponentService;
import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.login.Users;
import com.example.application.data.orders.*;
import com.example.application.data.services.ServicesService;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Objects;
import java.util.Optional;

@Route(value = "work-orders-view")
@PageTitle("Список работ")
@Menu(order = 12, icon = LineAwesomeIconUrl.TOOLS_SOLID)

@RolesAllowed({"WORKS", "GOD"})
public class WorkOrdersView extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;
    private final OrdersService orderService;
    private final OrderServicesService orderServicesService; // Добавлено
    private final ServicesService servicesService;
    private final OrderComponentsService orderComponentsService; // Добавлено
    private final ComponentService componentService;
    private final WorkOrdersService workOrdersService;
    private final EmployeesService employeesService;

    private final Grid<WorkOrders> workOrderGrid = new Grid<>(WorkOrders.class);

    public WorkOrdersView(OrdersService orderService,
                          AuthenticatedUser authenticatedUser,
                          OrderServicesService orderServicesService,
                          ServicesService servicesService,
                          OrderComponentsService orderComponentsService,
                          ComponentService componentService,
                          WorkOrdersService workOrdersService,
                          EmployeesService employeesService) {
        this.orderService = orderService;
        this.authenticatedUser = authenticatedUser;
        this.orderServicesService = orderServicesService;
        this.servicesService = servicesService;
        this.orderComponentsService = orderComponentsService;
        this.componentService = componentService;
        this.workOrdersService = workOrdersService;
        this.employeesService = employeesService;

        initView();
    }

    private void initView() {
        workOrderGrid.removeAllColumns();
        workOrderGrid.addColumn(WorkOrders::getDateOfWorkOrder).setHeader("Дата наряда");
        workOrderGrid.addColumn(WorkOrders::getNumberOfWorkOrder).setHeader("Номер");
        workOrderGrid.addColumn(WorkOrders::getWorkOrderStatusName).setHeader("Статус");
        workOrderGrid.addColumn(workOrder -> {
            if (workOrder.getOrders() != null) {
                return workOrder.getOrders().getNumberOfOrder();
            }
            return "Не указано";
        }).setHeader("Заказ").setAutoWidth(true);
        workOrderGrid.addColumn(workOrder -> {
            if (workOrder.getEmployee() != null) {
                return workOrder.getEmployee().getFullName();
            }
            return "Не указан";
        }).setHeader("Мастер").setAutoWidth(true);
        workOrderGrid.addComponentColumn(this::createWorkOrderActions).setHeader("Действия").setWidth("250px");

        updateGrid();
        setSizeFull();
        add(workOrderGrid);
    }

    private void updateGrid() {
        workOrderGrid.setItems(workOrdersService.findAll()); // Загрузка данных
        workOrderGrid.getDataProvider().refreshAll(); // Принудительное обновление данных
    }


    private HorizontalLayout createWorkOrderActions(WorkOrders workOrder) {
        Button editBtn = new Button("Открыть", VaadinIcon.EDIT.create(), e -> showWorkOrderForm(workOrder));

        Optional<Users> maybeUser = authenticatedUser.get();
        Users user = maybeUser.get();
        editBtn.setVisible(Objects.equals(user.getUsername(), "su") || Objects.equals(user.getUsername(), "worker"));

        editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editBtn.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        return new HorizontalLayout(editBtn);
    }

    private void showWorkOrderForm(WorkOrders workOrder) {
        // Проверки сотрудника и локации для новых заказов
        if (workOrder.getId() == null) {
            Optional<Users> maybeUser = authenticatedUser.get();
            if (maybeUser.isEmpty()) {
                Notification.show("Ошибка: Пользователь не найден", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            Users user = maybeUser.get();
            Employees employee = user.getEmployee();
            if (employee == null) {
                Notification.show("Ошибка: У пользователя нет привязанного сотрудника", 3000, Notification.Position.TOP_CENTER);
                return;
            }

        }

        // Создание диалога
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        dialog.setHeaderTitle(workOrder.getId() == null ? "Новый заказ"
                : "Редактирование заказа #" + workOrder.getNumberOfWorkOrder() + " от " + workOrder.getDateOfWorkOrder() + ", статус: " + workOrder.getWorkOrderStatusName());

        WorkOrderForm form = new WorkOrderForm(
                workOrder,
                orderService,
                orderServicesService,
                servicesService,
                orderComponentsService,
                componentService,
                workOrdersService,
                employeesService,
                () -> {
                    updateGrid();               // Обновляем сетку
                    dialog.close();             // Закрываем диалог
                    Notification.show("Наряд сохранен", 3000, Notification.Position.TOP_CENTER);
                },
                () -> {
                    dialog.close();              // Просто закрываем диалог при отмене
                }


        );
        dialog.setWidth("1500px");
        dialog.add(form);
        dialog.open();
    }
}