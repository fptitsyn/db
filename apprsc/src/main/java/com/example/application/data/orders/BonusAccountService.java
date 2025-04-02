package com.example.application.data.orders;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BonusAccountService {
    private final BonusAccountRepository bonusAccountRepository;

    public BonusAccountService(BonusAccountRepository bonusAccountRepository) {
        this.bonusAccountRepository = bonusAccountRepository;
    }

    public Optional<BonusAccount> findByClientId(Long clientId) {
        return bonusAccountRepository.findByClientsId(clientId);
    }
}