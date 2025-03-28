package com.example.application.data.components;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Query("SELECT new com.example.application.data.components.ComponentDTO("
            + "c.componentId, "
            + "c.category.typeOfDevice.typeOfDeviceName, "
            + "c.category.typeOfPartName, "
            + "c.name, "
            + "c.cost) "
            + "FROM Component c")
    List<ComponentDTO> findAllComponents();
}