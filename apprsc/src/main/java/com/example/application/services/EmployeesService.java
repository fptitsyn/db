package com.example.application.services;

import com.example.application.data.Employees;
import com.example.application.data.EmployeesRepository;
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

    private final EmployeesRepository repository;

    public EmployeesService(EmployeesRepository repository) {
        this.repository = repository;
    }

    @Cacheable
    public Optional<Employees> get(Long id) {
        return repository.findById(id);
    }

    @CacheEvict(allEntries = true)
    public Employees save(Employees entity) {
        return repository.save(entity);
    }

    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Cacheable
    public Page<Employees> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Cacheable
    public Page<Employees> list(Pageable pageable, Specification<Employees> filter) {
        return repository.findAll(filter, pageable);
    }

    @Cacheable
    public List<Employees> findAll() {
        return repository.findAll();
    }

    @Cacheable
    public List<Employees> findAll(String filter) {
        if (filter.isEmpty()) {
            return repository.findAll();
        }
        String searchTerm = "%" + filter + "%";
        return repository.findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCase(
                searchTerm,
                searchTerm,
                searchTerm
        );
    }

    @Cacheable
    public int count() {
        return (int) repository.count();
    }
}