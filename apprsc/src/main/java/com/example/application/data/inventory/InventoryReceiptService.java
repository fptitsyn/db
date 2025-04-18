package com.example.application.data.inventory;

import com.example.application.data.components.ComponentRepository;
import com.example.application.data.locations.LocationsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryReceiptService {
    private final InventoryReceiptRepository receiptRepository;
    private final InventoryService inventoryService;
    private final ComponentRepository componentRepository;
    private final LocationsRepository locationsRepository;

    public InventoryReceiptService(InventoryReceiptRepository receiptRepository, InventoryService inventoryService,
                                   ComponentRepository componentRepository, LocationsRepository locationsRepository) {
        this.receiptRepository = receiptRepository;
        this.inventoryService = inventoryService;
        this.componentRepository = componentRepository;
        this.locationsRepository = locationsRepository;
    }

    @Transactional
    public void receiveComponent(Long componentId, Long locationId, int quantity) {
        InventoryReceipt receipt = new InventoryReceipt();
        receipt.setComponent(componentRepository.findById(componentId).orElseThrow());
        receipt.setLocations(locationsRepository.findById(locationId).orElseThrow());
        receipt.setQuantity(quantity);
        receipt.setReceiptDate(LocalDate.now());
        receiptRepository.save(receipt);

        inventoryService.addToInventory(componentId, locationId, quantity);
    }

    public List<InventoryReceipt> getAllInventoryReceiptItems() {
        return receiptRepository.findAll();
    }
}