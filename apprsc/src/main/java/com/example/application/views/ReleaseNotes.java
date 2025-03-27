package com.example.application.views;


import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route(value = "ChangesInDevelopment")
@PageTitle("Список изменений")
@Menu(order = 101, icon = LineAwesomeIconUrl.LIST_UL_SOLID)

@RolesAllowed({"HR","WORKS","ADMIN","USER","SALES","GOD","ANALYSTS"})
public class ReleaseNotes extends VerticalLayout {
    public ReleaseNotes() {
        setSpacing(false);


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
        setSizeFull();
    }

}
