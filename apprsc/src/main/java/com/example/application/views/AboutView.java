package com.example.application.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;


@Route(value = "")
@PageTitle("О программе")
@Menu(order = 100, icon = LineAwesomeIconUrl.INFO_CIRCLE_SOLID)

@RolesAllowed({"HR", "WORKS", "ADMIN", "USER", "SALES", "GOD", "ANALYSTS"})

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

        add(new H5("22.04"));
        add(new Span("- WorkOrderForm. Кнопка Сменить исполнителя - Выбрать другого исполнителя, заполнить ему график, старому очистить график"));
        add(new H5("21.04"));
        add(new Span("- Триггер и функция для отслеживания времени последнего изменения в таблице Orders, Work_orders + Сортировка по этим полям в Формах."));
        add(new Span("- WorkOrderForm. Добавить дату в графики после ФИО"));
        add(new Span("- Склад. Добавлен order_id при списание (в Issue) (для понимания в рамках какого заказа)"));
        add(new Span("- Доработан roles_grants.sql - Добавлены права для продавцов и аналитиков"));
        add(new Span("- В перемещениях добавлены ограничения на привязку к должности в другом городе."));
        add(new Span("- В перемещениях добавлен контроль - один человек на место (берем нового, старому проставляем дату увольнения)."));

        add(new H5("19.04"));
        add(new Span("- Orders. Права: Кнопка открыть доступна менеджеру (тому кто указан как мастер в наряде) и GOD (те у кого роль - GOD) - все"));
        add(new H5("18.04"));
        add(new Span("- OrderForm. Списание со склада. Контроль наличия на складе."));
        add(new Span("- Склад"));
        add(new H5("17.04"));
        add(new Span("- OrderForm Распределение в работу"));
        add(new Span("- WorkOrders. Права: Кнопка открыть доступна мастеру (тому кто указан как мастер в наряде) и GOD (те у кого роль - GOD) - все"));
        add(new Span("- WorkOrderForm. Кнопка Выполнено - проставлять в связанном Заказе - статус 3."));
        add(new H5("16.04"));
        add(new Span("- Привязка услуг к Рабочему"));
        add(new Span("- Отчетность по Orders (Офис, Менеджер, номер, дата, сумма тотал, сумма сучетом скидки, начисленно бонусов, списано бонусов, Статус)"));
        add(new Span("- Отчетность по WorkOrders (Мастер, номер, дата, кол-во услуг, сумма часов, Статус)"));
        add(new Span("- InvoiceForPayment, доработать триггер. Вставить запись в BonusAccountOperation (bonus_account_id)"));
        add(new Span("- InvoiceForPayment, доработать триггер. Client Статус логика изменения (Постоянный - сумма totalCost = 100К и Премиум - сумма totalCost = 500К )"));
        add(new Span("- WorkOrders. Права: Кнопка открыть только пользователю su и worker"));
        add(new Span("- WorkOrderForm. Добавить Номер, Дата, Графики, два Grid'а для отображения Услуг и Компонент"));
        add(new Span("- WorkOrderForm. добавить кнопки (Взять в работу (видна в 1 статусе, ставит статус 2), Наряд выполнен(видна в 2 статусе, ставит статус 3))"));
        add(new H5("15.04"));
        add(new Span("- ScheduleView при добавление/удаление графиков после выбор офиса Доступны для выбора только сотрудники работающие в этих офисах"));
        add(new Span("- Создана Функция в базе которая по номеру заказа возвращает список Офисов и сотрудников где можно выполнить заказ. Отображается по  кнопке (Где починить?) в OrderForm"));
        add(new H5("14.04"));
        add(new Span("- OrdersForm при оплате контроль списание бонусов не больше чем накоплено"));
        add(new Span("- OrderView Исправлено: не обновлялся Grid после возврата из заказа"));
        add(new Span("- OrdersForm скрыты кнопки удалить сервисы/компоненты после 1-го статуса"));
        add(new Span("- В OrderForm скрыта кнопка Сохранить после 2го статуса. Все сохранение в других кнопках (распределить, оплатить, отменить)"));
        add(new Span("- Добавлено отображение чеков в оплаченные заказы"));
        add(new Span("- Создан roles_grants.sql - Роли и права для работы в БД"));
        add(new H5("12.04"));
        add(new Span("- Оплата заказа. Формирование чека. Начисление/списание бонусов"));
        add(new Span("- ClientForm при создание клиента, выбор м/ж из списка"));
        add(new H5("11.04"));
        add(new Span("- Выравнивание сумм по правому краю в Grid'ах (.setTextAlign(ColumnTextAlign.END)) + отображение основания для начисления/списания бонусов (BonusForm)"));
        add(new H5("10.04"));
        add(new Span("- OrderForm подсчет и сохранение суммы итого по заказу"));
        add(new Span("- Новая Entity InvoiceForPayment Счет по заказу"));
        add(new Span("- ClientStatus. Добавлен % для начисления бонусов (Обычный-1%;Постоянный-3%;Премиум-5%)"));
        add(new Span("- Драфт механизма по созданию WorkOrders (всегда создается на Сотрудника с ID=1)"));
        add(new Span("- WorkOrders новое Представление"));
        add(new Span("- Открытие на весь экран (ClientsView, WorkOrdersView, OrdersView, BonusForm)"));
        add(new Span("- Реализована Функция которая возвращает сумму доступных бонусов"));
        add(new Span("- BonusView доработана. Выведено: все операции (BonusAccountOperation) + Доступно бонусов (в Футере через функцию)"));
        add(new Span("- InvoiceForPayment, создан триггер после вставки: Вставить запись в BonusAccountOperation (списать (с минусом) или зачислить)"));

        add(new H5("09.04"));
        add(new Span("- OrderForm Добавлена логика  скрытия кнопок в зависимости от статуса"));
        add(new Span("- ClientForm Добавлено поле статус. Создан триггер при создание с присвоением статуса = 1 (Обычный)"));
        add(new Span("- Переведено всё исчисляемое время из минут в часы"));
        add(new Span("- WorkOrders. Создать триггер и функция по первичному присвоения Даты, номера, статуса при создании"));
        add(new Span("- Поменяно везде где есть рубли тип на одинаковый (BigDecimal): Зарплата, стоимость услуг, компонент..."));
        add(new Span("- Отчётность по Locations - Добавлено количество человек в офисе"));
        add(new H5("08.04"));
        add(new Span("- Orders. Созданы триггер и функция по первичному присвоения Даты, номера, статуса при создании. Поправлено отображение этих полей в OrderView"));
        add(new Span("- Создан Защита от дубликатов в расписание - уникальный индекс"));
        add(new Span("- Созданы процедуры для создания/удаления расписания для сотрудник в конкретном офисе на день(с проверкой существующих записей)."));
        add(new Span("- Создана Функция по получению расписания по всем сотрудникам в определенном офисе на дату"));
        add(new Span("- Добавлена Entity Schedule. ScheduleView. Просмотр графиков о офисе. Создание/удаление графиков"));
        add(new H5("07.04"));
        add(new Span("- Добавлены поля в форму Clients"));
        add(new Span("- Добавлены поля в форму Orders. Добавлено суммирование в Grid"));
        add(new H5("03.04"));
        add(new Span("- Созданы Entity: OrderServices, OrderComponents, WorkOrders"));
        add(new Span("- Создан прототип заказа который позволяет сохранять OrderServices, OrderComponents"));

        add(new H5("02.04"));
        add(new Span("- Добавлена новая функция для заполнения даты открытия бонусного счета"));
        add(new Span("- Employees, добавлено в Форму - пол. Переделана генерация"));
        add(new Span("- OrderView исправлена кнопка (Изменить). Можно было нажать несколько раз. (На проверке)"));
        add(new Span("- Добавлена отчетность по Locations"));
        add(new Span("- LocationsView доработано отображение и редактирование недостающих полей"));
        add(new H5("01.04"));
        add(new Span("- Созданы OrderStatus, WorkOrderStatus, ClientStatus, BonusAccountOperation.  Добавлен пол в Clients и Employees"));
        add(new Span("- В data.sql добавлены скрипты для заполнения  OrderStatus, WorkOrderStatus, ClientStatus"));
        add(new Span("- Добавлены фильтры для поиска клиента"));
        add(new Span("- В OrderForm, BonusForm добавлена кнопку (Вернуться к списку клиентов)"));
        add(new H5("27.03"));
        add(new Span("- StaffingTableView Добавлено поле (Название подразделения)"));
        add(new Span("- OrderForm При создание автоматически присваивать employeeId и location_id в зависимости от пользователя"));
        add(new H5("26.03"));
        add(new Span("- Изменена привязка услуг к сотруднику"));
        add(new Span("- Тестирование гита"));
        add(new Span("- Изменен внешний вид справочников компонентов (3шт)"));
        add(new Span("- MainView. Добавлена кнопка (Бонус). Создана форма BonusView новая форма для информации про бонусный счет"));
        add(new Span("- Изменена Entity Client"));
        add(new Span("- Добавлены формы ReleaseNotes и ToDoView"));
        add(new Span("- Добавлена дата в BonusAccount, созданы: BonusAccountRepository и BonusAccountService"));
        add(new Span("- Изменено окно (О программе)"));
        add(new H5("25.03"));
        add(new Span("- ServicesView. Новая форма для заведение и редактирования услуг"));
        add(new Span("- EmployeesView. Генерация случайных сотрудников"));
        add(new Span("- UsersView. Добавлена возможность привязать сотрудника. Исправлен Users (убран Абстрактный класс)"));
        add(new Span("- Добавлена сущность BonusAccount. Создана Функция и триггер для присвоения номера бонусного счета"));
        add(new Span("- Создана функция и триггер создающие Бонусный счет после создания клиента"));
        add(new Span("- Добавлена роль Analysts"));
        add(new H5("21.03"));
        add(new Span("- Справочники: Компоненты, Комплектующие, Типы устройств"));
        add(new H5("21.03"));
        add(new Span("- Базовые формы для создания Клиентов и Заказов"));
        add(new H5("19.03"));
        add(new Span("- Отчет по сотрудникам. Выгрузка в Excel"));
        add(new H5("18.03"));
        add(new Span("- Перемещения сотрудников"));
        add(new H5("17.03"));
        add(new Span("- Штатное расписание"));
        add(new H5("13.03"));
        add(new Span("- LocationsView. Справочник Офисов"));
        add(new Span("- EmployeesView. Справочник Сотрудников"));
        add(new H5("13.03"));
        add(new Span("- UsersView. Сохранение пароля(шифрованного)"));
        add(new H5("12.03"));
        add(new Span("- UsersView. Справочник пользователей"));
        add(new H5("10.03"));
        add(new Span("- Создан проект"));
        add(new Paragraph("=============================================================================="));
        setSizeFull();
    }

}