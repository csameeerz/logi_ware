package com.project.logiware.inventory.service;

import com.project.logiware.inventory.model.Inventory;
import com.project.logiware.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode).orElse(null);
        if (inventory != null) {
            return inventory.getQuantity() > 0;
        } else {
            return false;
        }
    }
}
