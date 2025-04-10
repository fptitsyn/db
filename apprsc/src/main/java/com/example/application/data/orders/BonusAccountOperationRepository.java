package com.example.application.data.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BonusAccountOperationRepository extends JpaRepository<BonusAccountOperation, Long> {
    List<BonusAccountOperation> findAllByBonusAccountId(Long bonusAccountId);

    @Query(value = "SELECT get_all_available_bonuses(:bonusAccountId)", nativeQuery = true)
    BigDecimal CalculateTotalBonuses(@Param("bonusAccountId") Long BonusAccountId);
}
