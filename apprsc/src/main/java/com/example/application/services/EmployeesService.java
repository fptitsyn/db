package com.example.application.services;

import com.example.application.data.Employees;
import com.example.application.data.EmployeesRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EmployeesService  {

    private final EmployeesRepository repository;

    public EmployeesService(EmployeesRepository repository) {
        this.repository = repository;
    }

    public Optional<Employees> get(Long id) {
        return repository.findById(id);
    }

    public Employees save(Employees entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Employees> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Employees> list(Pageable pageable, Specification<Employees> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
