package com.example.application.data.login;

import com.example.application.data.employees.Employees;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository repository;

    public UsersService(UsersRepository repository) {
        this.repository = repository;
    }

    public Optional<Users> get(Long id) {
        return repository.findById(id);
    }

    public Users save(Users entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Users> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Users> list(Pageable pageable, Specification<Users> filter) {
        return repository.findAll(filter, pageable);
    }
    public Page<Users> listWithEmployees(Pageable pageable) {
        return repository.findAll(pageable); // Будет использовать метод с EntityGraph
    }
    public boolean isEmployeeLinked(Employees employee) {
        return repository.existsByEmployee(employee);
    }
    public boolean isEmployeeLinkedToCurrentUser(Employees employee, Users currentUser) {
        return currentUser != null &&
                currentUser.getEmployee() != null &&
                currentUser.getEmployee().equals(employee);
    }
    public int count() {
        return (int) repository.count();
    }

}
