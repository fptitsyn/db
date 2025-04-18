package com.example.application.data.inventory;

import com.example.application.data.components.Component;
import com.example.application.data.components.ComponentRepository;
import com.example.application.data.locations.Locations;
import com.example.application.data.locations.LocationsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ComponentRepository componentRepository;
    private final LocationsRepository locationsRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                            ComponentRepository componentRepository,
                            LocationsRepository locationsRepository) {
        this.inventoryRepository = inventoryRepository;
        this.componentRepository = componentRepository;
        this.locationsRepository = locationsRepository;
    }

    @Transactional
    public void addToInventory(Long componentId, Long locationId, int quantity) {
        Component component = componentRepository.findById(componentId)
                .orElseThrow(() -> new RuntimeException("Component not found"));
        Locations location = locationsRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Inventory inventory = inventoryRepository.findByComponentAndLocations(component, location)
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setComponent(component);
                    newInventory.setLocations(location);
                    newInventory.setQuantity(0);
                    return newInventory;
                });

        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void removeFromInventory(Long componentId, Long locationId, int quantity) {
        Component component = componentRepository.findById(componentId)
                .orElseThrow(() -> new RuntimeException("Компонент не найден"));
        Locations location = locationsRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Локация не найдена"));

        Inventory inventory = inventoryRepository.findByComponentAndLocations(component, location)
                .orElseThrow(() -> new InventoryException(
                        component.getName(),
                        quantity
                ));

        if (inventory.getQuantity() < quantity) {
            throw new InventoryException(
                    component.getName(),
                    quantity - inventory.getQuantity()
            );
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    public List<Inventory> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }
}