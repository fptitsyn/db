package com.example.application.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import jakarta.annotation.security.RolesAllowed;


@Route(value = "")
@PageTitle("О программе")
@Menu(order = 100, icon = LineAwesomeIconUrl.INFO_CIRCLE_SOLID)

@RolesAllowed({"HR","WORKS","ADMIN","USER","SALES","GOD","ANALYSTS"})

public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);
        add(new Paragraph("=============================================================================="));
        add(new H2("Решили поработать?!!"));
        add(new Paragraph("=============================================================================="));
        add(new Span("Вам повезло - это самая крутая программа в мире! Спасибо разработчикам 🤗"));
        add(new Span("Админы - админят! Продаваны - продают! Работяги - работают! Кадровики - кадрят! Аналитики - аналичат!"));
        add(new Span("И за всем ними следит GOD!"));
        add(new Paragraph(""));

        add(new Paragraph("=============================================================================="));
        add(new H2("Список изменений"));
        add(new Paragraph("=============================================================================="));

        add(new H5("03.04"));
        add(new Span("Созданы Entity: OrderServices, OrderComponents, WorkOrders"));
        add(new H5("02.04"));
        add(new Span("Добавлена новая функция для заполнения даты открытия бонусного счета"));
        add(new Span("Employees, добавлено в Форму - пол. Переделана генерация"));
        add(new Span("OrderView исправлена кнопка (Изменить). Можно было нажать несколько раз. (На проверке)"));
        add(new Span("Добавлена отчетность по Locations"));
        add(new Span("LocationsView доработано отображение и редактирование недостающих полей"));
        add(new H5("01.04"));
        add(new Span("Созданы OrderStatus, WorkOrderStatus, ClientStatus, BonusAccountOperation.  Добавлен пол в Clients и Employees"));
        add(new Span("В data.sql добавлены скрипты для заполнения  OrderStatus, WorkOrderStatus, ClientStatus."));
        add(new Span("Добавлены фильтры для поиска клиента"));
        add(new Span("В OrderForm, BonusForm добавлена кнопку (Вернуться к списку клиентов)"));
        add(new H5("27.03"));
        add(new Span("StaffingTableView Добавлено поле (Название подразделения)"));
        add(new Span("OrderForm При создание автоматически присваивать employeeId и location_id в зависимости от пользователя"));
        add(new H5("26.03"));
        add(new Span("Изменена привязка услуг к сотруднику"));
        add(new Span("Тестирование гита"));
        add(new Span("Изменен внешний вид справочников компонентов (3шт)"));
        add(new Span("MainView. Добавлена кнока (Бонус). Создана форма BonusView новая форма для информации про бонусный счет"));
        add(new Span("Изменена Entity Client"));
        add(new Span("Добавлены формы ReleaseNotes и ToDoView"));
        add(new Span("Добавлена дата в BonusAccount, созданы: BonusAccountRepository и BonusAccountService "));

        add(new Span("Изменено окно (О программе)"));
        add(new H5("25.03"));
        add(new Span("ServicesView. Новая форма для заведение и редактирования услуг"));
        add(new Span("EmployeesView. Генерация случайных сотрудников"));
        add(new Span("UsersView. Добавлена возможность привязать сотрудника. Исправлен Users (убран Абстрактный класс)"));
        add(new Span("Добавлена сущность BonusAccount. Создана Функция и триггер для присвоения номера бонусного счета"));
        add(new Span("Создана функция и триггер создающие Бонусный счет после создания клиента"));
        add(new Span("Добавлена роль Analysts"));
        add(new H5("21.03"));
        add(new Span("Справочники: Компоненты, Комлектующие, Типы устройств"));
        add(new H5("21.03"));
        add(new Span("Базовые формы для создания Клиентов и Заказов"));
        add(new H5("19.03"));
        add(new Span("Отчет по сотрудникам. Выгрузка в Excell"));
        add(new H5("18.03"));
        add(new Span("Перемещения сотрудников"));
        add(new H5("17.03"));
        add(new Span("Штатное расписание"));
        add(new H5("13.03"));
        add(new Span("LocationsView. Справочник Офисов"));
        add(new Span("EmployeesView. Справочник Сотрудников"));
        add(new H5("13.03"));
        add(new Span("UsersView. Сохранение пароля(шифрованого)"));
        add(new H5("12.03"));
        add(new Span("UsersView. Справочник пользователей"));
        add(new H5("10.03"));
        add(new Span("Создан проект"));
        add(new Paragraph("=============================================================================="));
        setSizeFull();
    }

}