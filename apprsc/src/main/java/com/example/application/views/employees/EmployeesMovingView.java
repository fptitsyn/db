package com.example.application.views.employees;

import com.example.application.data.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Перемешение сотрудников")
@Route("EmployeesMoving")
@UIScope
@Menu(order = 42, icon = LineAwesomeIconUrl.FILE_CONTRACT_SOLID)
@RolesAllowed({"HR","GOD"})
public class EmployeesMovingView extends VerticalLayout {
    private final StaffingTableRepository staffingTableRepository;
    private final EmployeesRepository employeesRepository;
    private final EmployeesMovingRepository employeesMovingRepository;

    private StaffingTable selectedStaffingTable;
    private Binder<EmployeesMoving> binderForMoving;

    private Grid<StaffingTable> grid = new Grid<>(StaffingTable.class);
    private Grid<EmployeesMoving> historyGrid = new Grid<>(EmployeesMoving.class);
    private FormLayout bindingForm;

    // Form fields
    private ComboBox<Employees> employeeCombo = new ComboBox<>("Сотрудник");
    private DatePicker employmentDate = new DatePicker("Дата приема");
    private DatePicker dismissalDate = new DatePicker("Дата увольнения");
    private Button bindButton = new Button("Привязать");

    // Добавляем кнопки для редактирования и удаления
    private Button editButton = new Button("Изменить");
    private Button deleteButton = new Button("Удалить");

    public EmployeesMovingView(StaffingTableRepository staffingTableRepository,
                             EmployeesRepository employeesRepository,
                             EmployeesMovingRepository employeesMovingRepository) {

        this.staffingTableRepository = staffingTableRepository;
        this.employeesRepository = employeesRepository;
        this.employeesMovingRepository = employeesMovingRepository;

// Инициализация компонентов ДО их использования
        grid = new Grid<>(StaffingTable.class);
        historyGrid = new Grid<>(EmployeesMoving.class);
        bindingForm = new FormLayout();

        configureGrid();
        add(grid);

        configureBindingForm();
        configureHistoryGrid(); // Добавляем настройку грида истории
        add(new HorizontalLayout(bindingForm), historyGrid);

        updateGrid();
        updateHistory(); // Первоначальная загрузка всей истории
        setSizeFull();
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(StaffingTable::getPosition)
                .setHeader("Должность")
                .setAutoWidth(true);
        grid.addColumn(StaffingTable::getSalary)
                .setHeader("ФОТ")
                .setAutoWidth(true);
        grid.addColumn(l -> {
                    Locations type = l.getLocation();
                    return type != null ? type.getName() : "—";
                })
                .setHeader("Офис")
                .setAutoWidth(true);

        // Обработчик выбора должности
        grid.asSingleSelect().addValueChangeListener(e -> {
            selectedStaffingTable = e.getValue();
            bindingForm.setVisible(selectedStaffingTable != null);
            updateHistory(); // Обновляем историю при выборе должности
            clearForm(); // Очищаем форму
        });
    }

    private void configureBindingForm() {
        bindingForm = new FormLayout();
        binderForMoving = new Binder<>(EmployeesMoving.class);

        employeeCombo.setItems(employeesRepository.findAll());
        employeeCombo.setItemLabelGenerator(e -> e.getLastName() + " " + e.getFirstName());

        // Привязка данных к полям формы
        binderForMoving.bind(employmentDate, EmployeesMoving::getDateOfEmployment, EmployeesMoving::setDateOfEmployment);
        binderForMoving.bind(dismissalDate, EmployeesMoving::getDateOfDismissal, EmployeesMoving::setDateOfDismissal);

        // Обработчик выбора сотрудника
        employeeCombo.addValueChangeListener(e -> {
            clearFormOnlyDate(); // Очищаем форму
        });

        // Кнопки
        HorizontalLayout buttons = new HorizontalLayout(bindButton, editButton, deleteButton);
        bindingForm.add(employeeCombo, employmentDate, dismissalDate, buttons);
        bindingForm.setVisible(false);

        // Обработчик кнопки "Привязать"
        bindButton.addClickListener(e -> {
            if (selectedStaffingTable != null && employeeCombo.getValue() != null) {
                EmployeesMoving moving = new EmployeesMoving();
                moving.setStaffingTable(selectedStaffingTable);
                moving.setEmployee(employeeCombo.getValue());
                moving.setDateOfEmployment(employmentDate.getValue());
                moving.setDateOfDismissal(dismissalDate.getValue());

                employeesMovingRepository.save(moving);
                Notification.show("Привязка сохранена");
                updateHistory();
                clearForm();
            }
        });

        // Обработчик кнопки "Изменить"
        editButton.addClickListener(e -> {
            EmployeesMoving selected = historyGrid.asSingleSelect().getValue();
            if (selected != null) {
                // Обновляем данные из формы
                binderForMoving.writeBeanIfValid(selected); // Сохраняем изменения из формы в объект
                employeesMovingRepository.save(selected); // Сохраняем объект в БД
                Notification.show("Изменения сохранены");
                updateHistory();
            } else {
                Notification.show("Выберите перемещение для редактирования", 3000, Notification.Position.BOTTOM_START);
            }
        });

        // Обработчик кнопки "Удалить"
        deleteButton.addClickListener(e -> {
            EmployeesMoving selected = historyGrid.asSingleSelect().getValue();
            if (selected != null) {
                ConfirmDialog dialog = new ConfirmDialog(
                        "Подтверждение удаления",
                        "Вы уверены что хотите удалить это перемещение?",
                        "Да", confirmEvent -> {
                    employeesMovingRepository.delete(selected);
                    Notification.show("Перемещение удалено");
                    updateHistory();
                    clearForm();
                },
                        "Нет", cancelEvent -> {}
                );
                dialog.open();
            } else {
                Notification.show("Выберите перемещение для удаления", 3000, Notification.Position.BOTTOM_START);
            }
        });
    }

    private void updateHistory() {
        if (selectedStaffingTable != null) {
            // Показываем историю для выбранной должности
            historyGrid.setItems(
                    employeesMovingRepository.findByStaffingTable(selectedStaffingTable)
            );
        } else {
            // Показываем всю историю, если ничего не выбрано
            historyGrid.setItems(employeesMovingRepository.findAll());
        }
    }

    private void clearForm() {
        employeeCombo.clear();
        employmentDate.clear();
        dismissalDate.clear();
        historyGrid.asSingleSelect().clear(); // Снимаем выделение в гриде истории
    }
    private void clearFormOnlyDate() {
        employmentDate.clear();
        dismissalDate.clear();
        historyGrid.asSingleSelect().clear(); // Снимаем выделение в гриде истории
    }

    private void configureHistoryGrid() {
        historyGrid.removeAllColumns();

        historyGrid.addColumn(m -> m.getEmployee().getFullName())
                .setHeader("Сотрудник")
                .setAutoWidth(true);

        historyGrid.addColumn(m -> m.getStaffingTable().getPosition())
                .setHeader("Должность")
                .setAutoWidth(true);

        historyGrid.addColumn(EmployeesMoving::getDateOfEmployment)
                .setHeader("Дата приема")
                .setAutoWidth(true);

        historyGrid.addColumn(EmployeesMoving::getDateOfDismissal)
                .setHeader("Дата увольнения")
                .setAutoWidth(true);

        // Обработчик выбора записи в истории
        historyGrid.asSingleSelect().addValueChangeListener(e -> {
            EmployeesMoving selected = e.getValue();
            if (selected != null) {
                populateForm(selected);
            }
        });
    }

    private void populateForm(EmployeesMoving moving) {
        if (moving != null) {
            // Устанавливаем сотрудника в ComboBox
            employeeCombo.setValue(moving.getEmployee());

            // Устанавливаем даты через Binder
            binderForMoving.readBean(moving); // Заполняем форму данными из выбранного перемещения
        }
    }

    private void updateGrid() {
        grid.setItems(staffingTableRepository.findAll());
    }
}