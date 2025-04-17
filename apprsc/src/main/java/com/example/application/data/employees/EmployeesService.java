package com.example.application.data.employees;

import com.example.application.data.orders.OfficeMasterDto;
import com.example.application.data.services.Services;
import com.example.application.data.services.ServicesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "employees")
public class EmployeesService {
    private final EmployeesRepository employeesRepository;
    private final ServicesRepository servicesRepository;
    private final EntityManager entityManager;

    @Autowired
    public EmployeesService(
            EmployeesRepository employeesRepository,
            ServicesRepository servicesRepository,
            EntityManager entityManager
    ) {
        this.employeesRepository = employeesRepository;
        this.servicesRepository = servicesRepository;
        this.entityManager = entityManager;
    }

    @Cacheable
    public Optional<Employees> get(Long id) {
        return employeesRepository.findById(id);
    }

    @CacheEvict(allEntries = true)
    public Employees save(Employees employee) {
        if (employee.getId() != null) {
            // Для существующего сотрудника загружаем текущую версию из БД
            Employees existing = employeesRepository.findById(employee.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

            // Копируем изменяемые поля
            existing.setFirstName(employee.getFirstName());
            existing.setComment(employee.getComment());
            existing.setEmail(employee.getEmail());
            existing.setDateOfBirth(employee.getDateOfBirth());
            existing.setLastName(employee.getLastName());
            existing.setMiddleName(employee.getMiddleName());
            existing.setPhone(employee.getPhone());
            existing.setGender(employee.getGender());

            // Обновляем связи
            existing.getServices().clear();
            existing.getServices().addAll(employee.getServices());

            return employeesRepository.save(existing);
        } else {
            // Для нового сотрудника
            return employeesRepository.save(employee);
        }
    }

    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        employeesRepository.deleteById(id);
    }

    @Cacheable
    public Page<Employees> list(Pageable pageable) {
        return employeesRepository.findAll(pageable);
    }

    @Cacheable
    public Page<Employees> list(Pageable pageable, Specification<Employees> filter) {
        return employeesRepository.findAll(filter, pageable);
    }

    @Cacheable
    public List<Employees> findAll() {
        return employeesRepository.findAll();
    }

    @Cacheable
    public List<Employees> findAll(String filter) {
        if (filter.isEmpty()) {
            return employeesRepository.findAll();
        }
        String searchTerm = "%" + filter + "%";
        return employeesRepository.findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCase(
                searchTerm,
                searchTerm,
                searchTerm
        );
    }
    public List<Services> findAllServices() {
        return servicesRepository.findAll();
    }


    @Cacheable
    public int count() {
        return (int) employeesRepository.count();
    }

    public Optional<Employees> findById(Long id) {
        return employeesRepository.findById(id);
    }

    public List<EmployeeWithPositionDTO> findEmployeesWithPositionByLocation(Long locationId) {
        return employeesRepository.findEmployeesWithPosition(locationId);
    }

    public List<OfficeMasterDto> getOrderEmployees(Long orderId) {
        try {
            List<Object[]> results = entityManager.createNativeQuery(
                            "SELECT * FROM get_order_employees(?1)")
                    .setParameter(1, orderId)
                    .getResultList();

            return results.stream()
                    .map(row -> new OfficeMasterDto(
                            getStringSafe(row[0]), // Офис
                            getStringSafe(row[2]), // Адрес
                            getStringSafe(row[3]), // Контакты
                            getStringSafe(row[4]), // ФИО
                            getStringSafe(row[5])  // Должность
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching order employees", e);
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Employees> getOrderEmployeesByLocation(Long orderId, Long locationId) {
        String sql = "SELECT employee_id FROM get_order_employees_by_location(?, ?)";
        List<Long> employeeIds = jdbcTemplate.queryForList(sql, new Object[]{orderId, locationId}, Long.class);
        return employeesRepository.findAllById(employeeIds);
    }

    private String getStringSafe(Object value) {
        return value != null ? value.toString().trim() : "";
    }
}