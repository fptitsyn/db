package com.example.application.data.orders;

import com.example.application.data.employees.Employees;
import jakarta.persistence.*;

import java.time.LocalDate;

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

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @OneToOne
    @JoinColumn(name = "work_order_status_id")
    private WorkOrderStatus workOrderStatus;

    public Long getWork_orders_id() {
        return work_orders_id;
    }

    public void setWork_orders_id(Long work_orders_id) {
        this.work_orders_id = work_orders_id;
    }

    public Long getNumberOfWorkOrder() {
        return numberOfWorkOrder;
    }

    public void setNumberOfWorkOrder(Long numberOfWorkOrder) {
        this.numberOfWorkOrder = numberOfWorkOrder;
    }

    public LocalDate getDateOfWorkOrder() {
        return dateOfWorkOrder;
    }

    public void setDateOfWorkOrder(LocalDate dateOfWorkOrder) {
        this.dateOfWorkOrder = dateOfWorkOrder;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public WorkOrderStatus getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(WorkOrderStatus workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }
}
