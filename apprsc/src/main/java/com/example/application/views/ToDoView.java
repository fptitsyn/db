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
        add(new Span("InvoiceForPayment, доработать триггер. Вставить запись в BonusAccountOperation (bonus_account_id)"));
        add(new Span("InvoiceForPayment, доработать триггер. Client Статус логика изменения (Постоянный - сумма totalCost = 100К и Премиум - сумма totalCost = 500К )"));

        add(new Span("WorkOrders. Права: Кнопка открыть доступна мастеру и GOD - все"));
        add(new Span("WorkOrderForm. Добавить Номер, Дата, Графики, два Grid'а для отображения Услуг и Компонент"));
        add(new Span("WorkOrderForm. добавить кнопки (Закрыть, Взять в работу (видна в 1 статусе, ставит статус 2), Наряд выполнен(видна в 2 статусе, ставит статус 3), Сменить исполнителя)"));
        add(new Span("WorkOrderForm. Кнопка Сменить исполнителя - Выбрать другого исполнителя, заполнить ему график, старому очистить график"));

        add(new Span("Отчетность по WorkOrders (Мастер, номер, дата, кол-во услуг, сумма часов, Статус)"));
        add(new Span("Отчетность по Orders (Офис, Менеджер, номер, дата, сумма тотал, сумма сучетом скидки, начисленно бонусов, списано бонусов, Статус)"));

        add(new Span("ERD for Database. Проверить схему на отсутствие ForeignKey - исправить Entity. Например order_services нет fk на order_id"));

        add(new Span("Создать Функцию в базе которая по номеру заказа возвращает список Офисов и сотрудников где можно выполнить заказ. Привязать вызов функции на кнопку (Где починить?) в OrderForm"));
        add(new Span("В перемещениях добавить ограничения на привязку к должности в другом городе."));
        add(new Span("Добавить отображение чеков в оплаченные заказы."));

        add(new H4("Нужное. Сложное"));
        add(new Span("Привязка услуг к Рабочему"));
        add(new Span("OrderForm Распределение в работу"));

        add(new Span("Склад"));
        add(new Span("Сгорание бонусов???"));

        add(new H4("Бантики"));
        add(new Span("В OrderForm не закрывать форму после нажатия кнопки сохранить, чтобы можно было добавить услуги и компоненты "));
        add(new Span("В OrderForm скрыть кнопку сохранить после 2го статуса"));
        add(new Span("ScheduleView при добавление/удаление графиков после выбор офиса показывать только сотрудников работающих в этих офисах"));
        add(new Span("OrderView не обновляется Grid после возвращения из изменения заказа"));
        add(new Span("ClientForm при создание клиента, выбор м/ж из списка"));
        add(new Span("ClientsView не обновляется Grid после возврата из заказа"));
        add(new Span("OrdersForm скрыть кнопки удалить сервисы/компоненты после 1-го статуса."));
        add(new Span("OrdersForm при оплате контролировать списание бонусов не больше чем есть."));
        setSizeFull();
    }
}
//Иконки https://icons8.com/line-awesome
