package com.example.application.services;

import com.example.application.data.Users;
import com.example.application.data.UsersRepository;

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

    public int count() {
        return (int) repository.count();
    }

}
