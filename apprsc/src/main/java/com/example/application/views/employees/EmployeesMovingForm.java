package com.example.application.views.employees;

import com.example.application.data.employees.*;
import com.example.application.data.locations.Locations;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@PageTitle("Приём/Увольнение")
@Route("EmployeesMoving")
@UIScope
//@Menu(order = 42, icon = LineAwesomeIconUrl.FILE_CONTRACT_SOLID)
@RolesAllowed({"HR", "GOD"})
public class EmployeesMovingForm extends VerticalLayout {
    private final StaffingTableRepository staffingTableRepository;
    private final EmployeesRepository employeesRepository;
    private final EmployeesMovingRepository employeesMovingRepository;

    private StaffingTable selectedStaffingTable;
    private Binder<EmployeesMoving> binderForMoving;

    private final Grid<StaffingTable> grid;
    private final Grid<EmployeesMoving> historyGrid;
    private FormLayout bindingForm;

    // Form fields
    private final ComboBox<Employees> employeeCombo = new ComboBox<>("Сотрудник");
    private final DatePicker employmentDate = new DatePicker("Дата приема");
    private final DatePicker dismissalDate = new DatePicker("Дата увольнения");
    private final Button bindButton = new Button("Привязать", VaadinIcon.LINK.create());

    // Добавляем кнопки для редактирования и удаления
    private final Button editButton = new Button("Изменить", VaadinIcon.CHECK_SQUARE_O.create());
    private final Button deleteButton = new Button("Удалить", VaadinIcon.TRASH.create());
    private final Button backButton = new Button("Вернуться назад");

    public EmployeesMovingForm(StaffingTableRepository staffingTableRepository,
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
        configureHistoryGrid();
        add(new H4("Привязанные сотрудники:"), historyGrid, new HorizontalLayout(bindingForm));

        add(backButton);
        configureBackButton();

        updateGrid();
        updateHistory(); // Первоначальная загрузка всей истории
        setSizeFull();
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(l -> {
                    Locations type = l.getLocation();
                    return type != null ? type.getName() : "—";
                })
                .setHeader("Офис")
                .setKey("office")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(StaffingTable::getDepartment)
                .setHeader("Подразделение")
                .setKey("department")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(StaffingTable::getPosition)
                .setHeader("Должность")
                .setKey("position")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(
                        new NumberRenderer<>(
                                StaffingTable::getSalary,
                                NumberFormat.getCurrencyInstance(Locale.of("ru", "RU"))
                        ))
                .setHeader("ФОТ")
                .setTextAlign(ColumnTextAlign.END);

        // Обработчик выбора должности
        grid.asSingleSelect().addValueChangeListener(e -> {
            selectedStaffingTable = e.getValue();
            UpdateEmployees(selectedStaffingTable.getLocation().getCity());
            bindingForm.setVisible(selectedStaffingTable != null);
            updateHistory(); // Обновляем историю при выборе должности
            clearForm(); // Очищаем форму
        });
        // Установка сортировки по умолчанию
        Grid.Column<StaffingTable> officeColumn = grid.getColumnByKey("office");
        Grid.Column<StaffingTable> departmentColumn = grid.getColumnByKey("department");
        Grid.Column<StaffingTable> positionColumn = grid.getColumnByKey("position");

        List<GridSortOrder<StaffingTable>> sortOrder = Arrays.asList(
                new GridSortOrder<>(officeColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(departmentColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(positionColumn, SortDirection.ASCENDING)
        );
        grid.setMultiSort(true);
        grid.sort(sortOrder);
    }

    private void UpdateEmployees(String city) {
        if (selectedStaffingTable != null && city != null) {
            List<Employees> availableEmployees = employeesRepository.findAvailableEmployees(
                    city,
                    selectedStaffingTable.getId()
            );

            employeeCombo.setItems(availableEmployees);
        }

        employeeCombo.setItemLabelGenerator(e -> {
            if (e == null) return "Неизвестный сотрудник";
            return (e.getLastName() != null ? e.getLastName() : "") + " " +
                    (e.getFirstName() != null ? e.getFirstName() : "");
        });
    }

    private void configureBindingForm() {
        bindingForm = new FormLayout();
        binderForMoving = new Binder<>(EmployeesMoving.class);

        // Привязка данных к полям формы
        binderForMoving.bind(employmentDate, EmployeesMoving::getDateOfEmployment, EmployeesMoving::setDateOfEmployment);
        binderForMoving.bind(dismissalDate, EmployeesMoving::getDateOfDismissal, EmployeesMoving::setDateOfDismissal);

        // Обработчик выбора сотрудника
        employeeCombo.addValueChangeListener(ignored -> {
            clearFormOnlyDate(); // Очищаем форму
        });

        HorizontalLayout employeesMovingEditor = new HorizontalLayout(employeeCombo, employmentDate, dismissalDate);
        // Кнопки
        HorizontalLayout buttons = new HorizontalLayout(bindButton, editButton, deleteButton);
        bindingForm.add(employeesMovingEditor);
        bindingForm.add(buttons);
        bindingForm.setVisible(false);

        bindingForm.setWidth("1400px");
        // Обработчик кнопки "Привязать"
        bindButton.addClickListener(ignored -> {
            if (selectedStaffingTable != null && employeeCombo.getValue() != null) {
                // Находим текущего сотрудника на этой должности (если есть)
                Optional<EmployeesMoving> currentEmployeeOpt = employeesMovingRepository
                        .findByStaffingTableAndDateOfDismissalIsNull(selectedStaffingTable);

                EmployeesMoving moving = new EmployeesMoving();
                moving.setStaffingTable(selectedStaffingTable);
                moving.setEmployee(employeeCombo.getValue());
                moving.setDateOfEmployment(employmentDate.getValue());

                // Если есть текущий сотрудник - увольняем его
                currentEmployeeOpt.ifPresent(current -> {
                    current.setDateOfDismissal(employmentDate.getValue());
                    employeesMovingRepository.save(current);
                    Notification.show("Предыдущий сотрудник " + current.getEmployee().getFullName() +
                            " уволен с должности " + selectedStaffingTable.getPosition());
                });

                employeesMovingRepository.save(moving);
                Notification.show("Новый сотрудник " + moving.getEmployee().getFullName() +
                        " принят на должность " + selectedStaffingTable.getPosition());

                updateHistory();
                clearForm();
                UpdateEmployees(selectedStaffingTable.getLocation().getCity());
            }
        });
        bindButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        bindButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        // Обработчик кнопки "Изменить"
        editButton.addClickListener(ignored -> {
            EmployeesMoving selected = historyGrid.asSingleSelect().getValue();
            if (selected != null) {
                // Обновляем данные из формы
                binderForMoving.writeBeanIfValid(selected); // Сохраняем изменения из формы в объект
                employeesMovingRepository.save(selected); // Сохраняем объект в БД
                Notification.show("Изменения сохранены");
                updateHistory();
                clearForm();
            } else {
                Notification.show("Выберите перемещение для редактирования", 3000, Notification.Position.BOTTOM_START);
            }
        });
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");

        // Обработчик кнопки "Удалить"
        deleteButton.addClickListener(ignored -> {
            EmployeesMoving selected = historyGrid.asSingleSelect().getValue();
            if (selected != null) {
                ConfirmDialog dialog = new ConfirmDialog(
                        "Подтверждение удаления",
                        "Вы уверены, что хотите удалить эту запись о сотруднике?",
                        "Да", confirmEvent -> {
                    // Проверяем, является ли сотрудник текущим на должности
                    boolean isActive = selected.getDateOfDismissal() == null;

                    if (isActive) {
                        // Если это активная запись, ищем предыдущего сотрудника
                        Optional<EmployeesMoving> previousEmployee = employeesMovingRepository
                                .findPreviousEmployee(selected.getStaffingTable(), selected.getDateOfEmployment());

                        if (previousEmployee.isPresent()) {
                            // Восстанавливаем предыдущего сотрудника
                            EmployeesMoving previous = previousEmployee.get();
                            previous.setDateOfDismissal(null);
                            employeesMovingRepository.save(previous);
                            Notification.show("Восстановлен предыдущий сотрудник: " +
                                    previous.getEmployee().getFullName());
                        }
                    }

                    employeesMovingRepository.delete(selected);
                    Notification.show("Запись удалена");
                    updateHistory();
                    clearForm();
                    UpdateEmployees(selectedStaffingTable.getLocation().getCity());
                },
                        "Нет", cancelEvent -> {}
                );
                dialog.setConfirmButtonTheme("error primary");
                dialog.open();
            } else {
                Notification.show("Выберите запись для удаления", 3000, Notification.Position.BOTTOM_START);
            }
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
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
        historyGrid.asSingleSelect().clear();
        bindButton.setEnabled(true);
        editButton.setEnabled(false);
        // Кнопка удаления остается активной, если есть выделенная запись
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

        // Добавляем колонку со статусом
        historyGrid.addColumn(m -> m.getDateOfDismissal() == null ? "Активен" : "Уволен")
                .setHeader("Статус")
                .setAutoWidth(true);

        // Обработчик выбора записи в истории
        historyGrid.asSingleSelect().addValueChangeListener(e -> {
            EmployeesMoving selected = e.getValue();
            if (selected != null) {
                populateForm(selected);
            }
        });
        historyGrid.setEmptyStateText("Не было принятых сотрудников");
    }

    private void populateForm(EmployeesMoving moving) {
        if (moving != null) {
            employeeCombo.setValue(moving.getEmployee());
            binderForMoving.readBean(moving);

            // Разрешаем удаление для любой записи
            deleteButton.setEnabled(true);

            // Блокируем редактирование/привязку для неактивных записей
            boolean isActive = moving.getDateOfDismissal() == null;
            bindButton.setEnabled(isActive);
            editButton.setEnabled(isActive);

            if (!isActive) {
                Notification.show("Этот сотрудник уже уволен с должности", 3000,
                        Notification.Position.BOTTOM_START);
            }
        }
    }

    private void updateGrid() {
        grid.setItems(staffingTableRepository.findAll());
    }

    private void configureBackButton() {
        backButton.setIcon(VaadinIcon.ARROW_BACKWARD.create());
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle()
                .set("margin-right", "1em")
                .set("color", "var(--lumo-primary-text-color)");
        backButton.addClickListener(ignored ->
                getUI().ifPresent(ui -> ui.navigate(HRView.class))
        );
    }
}