package com.example.application.views;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route(value = "To-Do-View")
@PageTitle("Нужно сделать")
@Menu(order = 102, icon = LineAwesomeIconUrl.EXCLAMATION_TRIANGLE_SOLID)

@RolesAllowed({"HR", "WORKS", "ADMIN", "USER", "SALES", "GOD", "ANALYSTS"})
public class ToDoView extends VerticalLayout {
    public ToDoView() {
        setSpacing(false);

        add(new H4("Нужное. Простое"));
        add(new Span("InvoiceForPayment, доработать триггер. Вставить запись в BonusAccountOperation (bonus_account_id)"));
        add(new Span("InvoiceForPayment, доработать триггер. Client Статус логика изменения (Постоянный - сумма totalCost = 100К и Премиум - сумма totalCost = 500К )"));

        add(new Span("WorkOrders. Права: Кнопка открыть доступна мастеру и GOD - все"));
        add(new Span("WorkOrderForm. Добавить Номер, Дата, Графики, два Grid'а для отображения Услуг и Компонент"));
        add(new Span("WorkOrderForm. добавить кнопки (Закрыть, Взять в работу (видна в 1 статусе, ставит статус 2), Наряд выполнен(видна в 2 статусе, ставит статус 3), Сменить исполнителя)"));
        add(new Span("WorkOrderForm. Кнопка Сменить исполнителя - Выбрать другого исполнителя, заполнить ему график, старому очистить график"));

        add(new Span("Отчетность по WorkOrders (Мастер, номер, дата, кол-во услуг, сумма часов, Статус)"));
        add(new Span("Отчетность по Orders (Офис, Менеджер, номер, дата, сумма тотал, сумма сучетом скидки, начисленно бонусов, списано бонусов, Статус)"));

        add(new Span("ERD for Database. Проверить схему на отсутствие ForeignKey - исправить Entity. Например order_services нет fk на order_id"));
        add(new Span("Доработать roles_grants.sql - Добавить права для продавцов и аналитиков"));

        add(new Span("В перемещениях добавить ограничения на привязку к должности в другом городе."));
        add(new Span("В перемещениях добавить контроль - один человек на место (берем нового, старому проставляем дату увольнения)."));

        add(new H4("Нужное. Сложное"));
        add(new Span("Привязка услуг к Рабочему"));
        add(new Span("OrderForm Распределение в работу"));

        add(new Span("Склад"));
        add(new Span("Сгорание бонусов???"));

        add(new H4("Бантики"));


        setSizeFull();
    }
}