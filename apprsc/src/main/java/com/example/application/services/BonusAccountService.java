package com.example.application.services;

import com.example.application.data.orders.BonusAccount;
import com.example.application.data.orders.BonusAccountRepository;
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