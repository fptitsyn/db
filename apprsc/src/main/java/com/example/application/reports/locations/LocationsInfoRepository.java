package com.example.application.reports.locations;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class LocationsInfoRepository {
    private final EntityManager entityManager;

    public LocationsInfoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<LocationsInfoDTO> findAllLocationInfo() {
        return entityManager.createNativeQuery(
                "SELECT * FROM get_location_info()",
                "LocationsInfoMapping" // Имя SQL mapping
        ).getResultList();
    }
}
