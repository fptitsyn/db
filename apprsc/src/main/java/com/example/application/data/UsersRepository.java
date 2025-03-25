package com.example.application.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

    Optional<Users> findByUsername(String username);

    @EntityGraph(attributePaths = {"employee", "roles"}) // Добавили загрузку ролей
    Page<Users> findAll(Pageable pageable);

    boolean existsByEmployee(Employees employee);
}