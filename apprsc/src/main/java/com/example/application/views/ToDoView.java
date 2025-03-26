package com.example.application.views;

import com.vaadin.flow.component.html.Paragraph;
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

@RolesAllowed({"HR","WORKS","ADMIN","USER","SALES","GOD","ANALYSTS"})
public class ToDoView extends VerticalLayout {
    public ToDoView() {
        setSpacing(false);

        add(new Span("В базе поправить Функцию для заполнения Даты открытия бонусного счета"));
        add(new Span("LocationsView доработать отображение и редактирование недостающих полей"));
        add(new Span("ClientForm Добавить все поля по клиенту"));
        add(new Span("MainView исправит кнопка (Ред.) Сейчас можно нажать несколько раз"));
        add(new Span("ClientStatus логика изменения"));
        add(new Span("OrderForm Формирование Заказа доработать"));
        add(new Span("OrderForm При создание автоматически присваивать employeeId и location_id в зависимости от пользователя"));
        add(new Span("OrderForm Добавить сверху ФИО клиента как в Бонусном счете (после добавления всех полей в клиента)"));
        add(new Span("OrderForm Добавить кнопку (Вернуться в клиента)"));
        add(new Span("OrderView исправит кнопка (Ред.) Сейчас можно нажать несколько раз"));
        add(new Span("Orders Статусы"));
        add(new Span("Заполнения графика работы"));
        add(new Span("BonusView Карточка бонусного счета Остаток/все операции (вместо заказов)"));
        add(new Span("BonusView Добавить кнопку (Вернуться в клиента)"));
        add(new Span("Привязка услуг к Рабочему"));
        add(new Span("В перемещениях добавить ограничения на привязку к должности в другом городе "));
        add(new Span("WorkOrders новое Представление"));
        add(new Span("WorkOrders права:рабочий видит свои, GOD - все"));
        add(new Span("WorkOrders Статусы"));
        add(new Span("OrderForm При создание автоматически присваивать employeeId и location_id в зависимости от пользователя"));
        add(new Span("Отчетность по WorkOrders"));
        add(new Span("Отчетность по Locations"));
        add(new Span("Склад"));
        add(new Span("Поиск клиента???"));
        add(new Span("Иконки https://icons8.com/line-awesome"));

        setSizeFull();
    }

}
