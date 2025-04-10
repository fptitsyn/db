package com.example.application.data.orders;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BonusAccountOperationService {
    private final BonusAccountOperationRepository bonusAccountOperationRepository;

    public BonusAccountOperationService(BonusAccountOperationRepository bonusAccountOperationRepository) {
        this.bonusAccountOperationRepository = bonusAccountOperationRepository;
    }

    public List<BonusAccountOperation> findAllByBonusAccountId(Long bonusAccountId) {
        return bonusAccountOperationRepository.findAllByBonusAccountId(bonusAccountId);
    }

    public BigDecimal getTotalBonuses(Long bonusAccountId) {
        return bonusAccountOperationRepository.CalculateTotalBonuses(bonusAccountId);
    }
}
