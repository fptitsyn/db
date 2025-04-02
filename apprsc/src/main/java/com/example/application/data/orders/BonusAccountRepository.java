package com.example.application.data.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BonusAccountRepository extends JpaRepository<BonusAccount, Long> {
    // Находим BonusAccount по ID клиента (поле clients в BonusAccount)
    Optional<BonusAccount> findByClientsId(Long clientId);
}