package com.example.application.data.employees;

import com.example.application.data.services.Services;
import com.example.application.data.services.ServicesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "employees")
public class EmployeesService {

    private final EmployeesRepository employeesRepository;

    private final ServicesRepository servicesRepository;

    public EmployeesService(EmployeesRepository employeesRepository, ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
        this.employeesRepository = employeesRepository;
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
}