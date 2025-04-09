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
        add(new Span("ClientForm Добавить поля по клиенту (статус). Создать тригер при создание с присвоением статуса = 1 (Обычный)"));
        add(new Span("BonusView Карточка бонусного счета Остаток/все операции (BonusAccountOperation) (вместо заказов)"));

        //add(new Span("ERD for Database. Проверить схему на отсутствие ForeignKey - исправить Entity. Например order_services нет fk на order_id")); ?

        add(new Span("Отчетность по Locations - добавить количество человек в офисе"));
        add(new Span("Отчетность по WorkOrders"));
        add(new Span("Создать Функцию в базе которая по номеру заказа возвращает список Офисов и сотрудников где можно выполнить заказ. Привязать вызов функции на кнопку (Где починить?) в OrderForm"));
        add(new Span("В перемещениях добавить ограничения на привязку к должности в другом городе "));

        add(new H4("Нужное. Сложное"));
        add(new Span("Привязка услуг к Рабочему"));
        add(new Span("OrderForm Распределение в работу"));
        add(new Span("WorkOrders новое Представление"));
        add(new Span("WorkOrders права:рабочий видит свои, GOD - все"));
        add(new Span("WorkOrders Статус логика изменения"));
        add(new Span("Склад"));
        add(new Span("Выдача. Оплата  услуг и комлектующих. Начисление бонус и списание"));
        add(new Span("Client Статус логика изменения (Постоянный и Премиум)"));
        add(new Span("Сгорание бонусов???"));

        add(new H4("Бантики"));
        add(new Span("В OrderForm не закрывать форму после нажатия кнопки сохранить, чтобы можно было добавить услуги и компоненты "));
        add(new Span("В OrderForm скрыть кнопку сохранить после 2го статуса"));
        add(new Span("ScheduleView при добавление/удаление графиков после выбоа офиса показывать только сотрудников работающих в этих офисах"));
        add(new Span("OrderView не обновляется Grid после возвращения из изменения заказа"));
        setSizeFull();
    }
}
//Иконки https://icons8.com/line-awesome
