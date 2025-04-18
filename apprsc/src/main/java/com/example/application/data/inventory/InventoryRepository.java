package com.example.application.data.inventory;

import com.example.application.data.components.Component;
import com.example.application.data.locations.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByComponentAndLocations(Component component, Locations locations);
}
