package com.example.application.reports.schedule;

import com.example.application.data.employees.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE " +
            "s.employee.id = :employeeId AND " +
            "s.workDay = :date AND " +
            "s.workOrders IS NULL")
    List<Schedule> findAvailableSlots(
            @Param("employeeId") Long employeeId,
            @Param("date") LocalDate date
    );
}