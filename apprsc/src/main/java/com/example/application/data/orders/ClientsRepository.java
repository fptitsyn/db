package com.example.application.data.orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {

    @Query("SELECT c FROM Clients c LEFT JOIN FETCH c.orders WHERE c.id = :id")
    Optional<Clients> findByIdWithOrders(@Param("id") Long id);
}