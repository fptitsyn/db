package com.example.application.reports.schedule;

import com.example.application.data.employees.Schedule;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {
    private final JdbcTemplate jdbcTemplate;
    private final ScheduleRepository scheduleRepository;



    public ScheduleService(JdbcTemplate jdbcTemplate, ScheduleRepository scheduleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.scheduleRepository = scheduleRepository;
    }

    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public List<ScheduleData> getSchedule(LocalDate workDay, Long locationId) {
        String sql = "SELECT * FROM get_schedule(?, ?)";
        return jdbcTemplate.query(sql, new ScheduleDataRowMapper(), workDay, locationId);
    }

    public List<ScheduleData> getScheduleByWorkOrderId(Long workOrderId) {
        String sql = "SELECT * FROM get_schedule_by_work_order_filtered(?)";
        return jdbcTemplate.query(sql, new ScheduleDataRowMapper(), workOrderId);
    }

    private static class ScheduleDataRowMapper implements RowMapper<ScheduleData> {
        @Override
        public ScheduleData mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ScheduleData(
                    rs.getString("employee_name"),
                    rs.getInt("09:00 - 10:00"),
                    rs.getInt("10:00 - 11:00"),
                    rs.getInt("11:00 - 12:00"),
                    rs.getInt("12:00 - 13:00"),
                    rs.getInt("14:00 - 15:00"),
                    rs.getInt("15:00 - 16:00"),
                    rs.getInt("16:00 - 17:00"),
                    rs.getInt("17:00 - 18:00"),
                    rs.getInt("total")
            );
        }
    }
    public void insertScheduleEntries(LocalDate workDay, Long employeeId, Long locationId) {
        jdbcTemplate.update(
                "CALL insert_schedule_entries(?, ?, ?)",
                workDay,
                employeeId,
                locationId
        );
    }

    public void deleteScheduleEntries(LocalDate workDay, Long employeeId, Long locationId) {
        try {
            jdbcTemplate.update(
                    "CALL delete_schedule_entries(?, ?, ?)",
                    workDay,
                    employeeId,
                    locationId
            );
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMostSpecificCause().getMessage());
        }
    }

    public List<Schedule> findAvailableSlots(Long employeeId, LocalDate date) {
        return scheduleRepository.findAvailableSlots(employeeId, date);
    }
}
