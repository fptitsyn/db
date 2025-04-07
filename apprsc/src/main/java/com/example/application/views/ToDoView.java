package com.example.application.views;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
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

        add(new H4("Нужное. Простое"));
        add(new Span("ClientForm Добавить поля по клиенту (статус)"));
        add(new Span("Отчетность по WorkOrders"));
        add(new Span("BonusView Карточка бонусного счета Остаток/все операции (BonusAccountOperation) (вместо заказов)"));
        add(new Span("Отчетность по Locations - добавить количество человек в офисе"));
        add(new Span("Orders. Создать триггер и функцию по первичному присвоения Даты, номера, Статуса при создании"));

        add(new H4("Нужное. Сложное"));
        add(new Span("Привязка услуг к Рабочему"));
        add(new Span("ClientStatus логика изменения: Первое присвоение, механизм изменения"));
        add(new Span("OrderForm Формирование Заказа доработать. Добавить все поля  (номер заказа, дата заказа)"));
        add(new Span("Orders Статус логика изменения"));
        add(new Span("Заполнения графика работы"));
        add(new Span("WorkOrders новое Представление"));
        add(new Span("WorkOrders права:рабочий видит свои, GOD - все"));
        add(new Span("WorkOrders Статус логика изменения"));
        add(new Span("Склад"));
        add(new Span("Выдача. Оплата  услуг и комлектующих. Начисление бонус и списание"));
        add(new Span("Сгорание бонусов???"));

        add(new H4("Бантики"));
        add(new Span("В перемещениях добавить ограничения на привязку к должности в другом городе "));
        //add(new Span("OrderView исправит кнопка (Изменить.) Сейчас можно нажать несколько раз")); - НА ПРОВЕРКЕ
        add(new Span("MainView исправит кнопка (Изменить.) Сейчас можно нажать несколько раз"));

        setSizeFull();
    }
}
//Иконки https://icons8.com/line-awesome
