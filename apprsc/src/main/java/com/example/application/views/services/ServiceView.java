package com.example.application.views.services;

import com.example.application.data.Services;
import com.example.application.services.ServicesService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Услуги")
@Route("services")
@Menu(order = 40, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed({"HR","GOD"})
public class ServiceView extends VerticalLayout {
    private final ServicesService servicesService;
    private Grid<Services> serviceGrid = new Grid<>(Services.class);

    public ServiceView(ServicesService servicesService) {
        this.servicesService = servicesService;
        initView();
        add(serviceGrid); // Добавляем Grid в layout
        setSizeFull(); // Устанавливаем полный размер
        loadServices(); // Загружаем данные
    }

    private void initView() {
        serviceGrid.removeAllColumns();
        serviceGrid.addColumn(Services::getService_name).setHeader("Услуга");
        serviceGrid.addColumn(Services::getCost).setHeader("Стоимость");
        serviceGrid.addColumn(Services::getTime_to_complete).setHeader("Время выполнения");
        serviceGrid.setSizeFull(); // Grid занимает все доступное пространство
    }

    private void loadServices() {
        // Получаем данные из сервиса и устанавливаем их в Grid
        serviceGrid.setItems(servicesService.findAllServices());
    }
}