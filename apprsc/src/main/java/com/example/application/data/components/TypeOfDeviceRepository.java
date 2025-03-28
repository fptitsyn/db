package com.example.application.data.components;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TypeOfDeviceRepository extends JpaRepository<TypeOfDevice, Long> {
    @Query("SELECT t FROM TypeOfDevice t WHERE t.typeOfDeviceName = :name")
    Optional<TypeOfDevice> findByTypeOfDeviceName(@Param("name") String name);
}
