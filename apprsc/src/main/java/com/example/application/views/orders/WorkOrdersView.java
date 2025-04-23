package com.example.application.views.orders;

import com.example.application.data.employees.Employees;
import com.example.application.data.employees.EmployeesService;
import com.example.application.data.locations.LocationsService;
import com.example.application.data.login.Role;
import com.example.application.data.login.Users;
import com.example.application.data.orders.*;
import com.example.application.reports.schedule.ScheduleService;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;
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
    private final ScheduleService scheduleService;
    private final OrderComponentsService orderComponentsService; // Добавлено
    private final WorkOrdersService workOrdersService;
    private final LocationsService locationsService;
    private final EmployeesService employeesService;

    private final Grid<WorkOrders> workOrderGrid = new Grid<>(WorkOrders.class);

    public WorkOrdersView(OrdersService orderService,
                          AuthenticatedUser authenticatedUser,
                          OrderServicesService orderServicesService,
                          ScheduleService scheduleService,
                          OrderComponentsService orderComponentsService,
                          WorkOrdersService workOrdersService,
                          LocationsService locationsService,
                          EmployeesService employeesService)    {
        this.orderService = orderService;
        this.authenticatedUser = authenticatedUser;
        this.orderServicesService = orderServicesService;
        this.scheduleService = scheduleService;
        this.orderComponentsService = orderComponentsService;
        this.workOrdersService = workOrdersService;
        this.locationsService = locationsService;
        this.employeesService = employeesService;

        initView();
    }

    private void initView() {
        workOrderGrid.removeAllColumns();
        workOrderGrid.addColumn(WorkOrders::getDateOfWorkOrder).setHeader("Дата наряда");
        workOrderGrid.addColumn(WorkOrders::getNumberOfWorkOrder).setHeader("Номер");

        Grid.Column<WorkOrders> statusColumn = workOrderGrid.addColumn(WorkOrders::getWorkOrderStatusName)
                .setHeader("Статус")
                .setSortable(true);

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

        Grid.Column<WorkOrders> lastModifiedCol = workOrderGrid.addColumn(WorkOrders::getLastModified).setHeader("Дата изменения").setSortable(true).setWidth("220px");
workOrderGrid.addComponentColumn(this::createWorkOrderActions)
                .setHeader("Действия")
                .setWidth("100px");

        // Настройка сортировки по умолчанию
        List<GridSortOrder<WorkOrders>> sortOrder = List.of(
                new GridSortOrder<>(lastModifiedCol, SortDirection.DESCENDING)
        );
        workOrderGrid.sort(sortOrder);


        updateGrid();
        setSizeFull();
        add(workOrderGrid);
    }

    private void updateGrid() {
        workOrderGrid.setItems(workOrdersService.findAll()); // Загрузка данных
        workOrderGrid.getDataProvider().refreshAll(); // Принудительное обновление данных
    }


    private HorizontalLayout createWorkOrderActions(WorkOrders workOrder) {
        Button editBtn = new Button("Открыть", VaadinIcon.EDIT.create(), ignored -> showWorkOrderForm(workOrder));

        Optional<Users> maybeUser = authenticatedUser.get();
        // Безопасная проверка наличия пользователя и установка видимости кнопки
        editBtn.setVisible(maybeUser.map(user ->
                        user.getRoles().contains(Role.GOD) ||
                                (workOrder.getEmployee() != null &&
                                        user.getEmployee() != null &&
                                        workOrder.getEmployee().getId().equals(user.getEmployee().getId())))
                .orElse(false));
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
        dialog.setHeaderTitle(workOrder.getId() == null ? "Новый наряд"
                : "Наряд #" + workOrder.getNumberOfWorkOrder() + " от " + workOrder.getDateOfWorkOrder() + ", статус: " + workOrder.getWorkOrderStatusName());

        WorkOrderForm form = new WorkOrderForm(
                workOrder,
                workOrdersService,
                scheduleService,
                orderServicesService,
                orderComponentsService,
                locationsService,
                employeesService,
                () -> {
                    updateWorkOrderStatus(workOrder);
                    updateGrid();               // Обновляем сетку
                    dialog.close();             // Закрываем диалог
                    Notification.show(createNotificationText(workOrder), 3000, Notification.Position.TOP_CENTER);
                },
                () -> {
                    dialog.close();              // Просто закрываем диалог при отмене
                }

        );
        dialog.setWidth("1500px");
        dialog.setHeight("1000px");
        dialog.add(form);
        dialog.open();
    }

    private void updateWorkOrderStatus(WorkOrders workOrder) {
        if (workOrder.getWorkOrderStatus().getId().intValue() == 3) {
            return;
        }

        WorkOrderStatus newStatus = workOrder.getWorkOrderStatus();

        newStatus.setId(newStatus.getId() + 1);
        workOrder.setWorkOrderStatus(newStatus);
        workOrdersService.save(workOrder);

        if (newStatus.getId().intValue() == 3) {
            Orders order = workOrder.getOrders();
            if (order.getOrderStatus().getId().intValue() == 3) {
                return;
            }

            order.setOrderStatusId(3L);
            orderService.save(order);
        }
    }

    private String createNotificationText(WorkOrders workOrder) {
        return switch (workOrder.getWorkOrderStatus().getId().intValue()) {
            case 2 -> "Наряд принят в работу";
            case 3 -> "Наряд выполнен";
            default -> "Ошибка!!!";
        };
    }
}