package com.example.application.data.orders;

import com.example.application.data.employees.Employees;
import com.example.application.data.employees.Schedule;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "work_orders")
public class WorkOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long work_orders_id;

    @Column(name = "number_of_work_order")
    private Long numberOfWorkOrder;

    @Column(name = "date_of_work_order")
    private LocalDate dateOfWorkOrder;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @ManyToOne
    @JoinColumn(name = "work_order_status_id")
    private WorkOrderStatus workOrderStatus;

    @OneToMany(mappedBy = "workOrders")
    private List<Schedule> schedules;

    public Long getId() {
        return work_orders_id;
    }
    public Long getNumberOfWorkOrder() {
        return numberOfWorkOrder;
    }
    public LocalDate getDateOfWorkOrder() {
        return dateOfWorkOrder;
    }
    public Orders getOrders() {
        return orders;
    }
    public Employees getEmployee() {
        return employee;
    }
    public WorkOrderStatus getWorkOrderStatus() {
        return workOrderStatus;
    }
    // Добавляем метод для получения названия статуса
    public String getWorkOrderStatusName() {
        return workOrderStatus != null ? workOrderStatus.getStatus() : "";
    }

    public void setId(Long work_orders_id) {
        this.work_orders_id = work_orders_id;
    }
    public void setNumberOfWorkOrder(Long numberOfWorkOrder) {
        this.numberOfWorkOrder = numberOfWorkOrder;
    }
    public void setDateOfWorkOrder(LocalDate dateOfWorkOrder) {
        this.dateOfWorkOrder = dateOfWorkOrder;
    }
    public void setOrders(Orders orders) {
        this.orders = orders;
    }
    public void setEmployee(Employees employee) {
        this.employee = employee;
    }
    public void setWorkOrderStatus(WorkOrderStatus workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }
}
