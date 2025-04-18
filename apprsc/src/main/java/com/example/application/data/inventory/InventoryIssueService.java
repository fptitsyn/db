package com.example.application.data.inventory;

import com.example.application.data.components.ComponentRepository;
import com.example.application.data.locations.LocationsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryIssueService {
    private final InventoryIssueRepository issueRepository;
    private final InventoryService inventoryService;
    private final ComponentRepository componentRepository;
    private final LocationsRepository locationsRepository;

    public InventoryIssueService(InventoryIssueRepository issueRepository, InventoryService inventoryService,
                                 ComponentRepository componentRepository, LocationsRepository locationsRepository) {
        this.issueRepository = issueRepository;
        this.inventoryService = inventoryService;
        this.componentRepository = componentRepository;
        this.locationsRepository = locationsRepository;
    }

    @Transactional
    public void issueComponent(Long componentId, Long locationId, int quantity) {
        InventoryIssue issue = new InventoryIssue();
        issue.setComponent(componentRepository.findById(componentId).orElseThrow());
        issue.setLocations(locationsRepository.findById(locationId).orElseThrow());
        issue.setQuantity(quantity);
        issue.setIssueDate(LocalDate.now());
        issueRepository.save(issue);

        inventoryService.removeFromInventory(componentId, locationId, quantity);
    }

    public List<InventoryIssue> getAllInventoryIssueItems() {
        return issueRepository.findAll();
    }
}