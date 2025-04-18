package com.example.application.data.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryIssueRepository extends JpaRepository<InventoryIssue, Long> {
}