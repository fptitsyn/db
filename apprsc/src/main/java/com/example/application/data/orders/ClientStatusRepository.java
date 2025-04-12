package com.example.application.data.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientStatusRepository extends JpaRepository<ClientStatus, Long> {
    @Query("SELECT c.clientStatus FROM Clients c WHERE c.client_id = :clientId")

    List<ClientStatus> findByClientId(@Param("clientId") Long clientId);
}