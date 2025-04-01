package com.example.application.reports.employees;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeInfoRepository {
    private final EntityManager entityManager;

    public EmployeeInfoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<EmployeeInfoDTO> findAllEmployeeInfo() {
        return entityManager.createNativeQuery(
                "SELECT * FROM get_employee_info()",
                "EmployeeInfoMapping" // Имя SQL mapping
        ).getResultList();
    }
}
