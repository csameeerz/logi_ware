package com.project.logiware.inventory.util;

import com.project.logiware.inventory.model.Inventory;
import com.project.logiware.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;
    @Override
    public void run(String... args) throws Exception {
        Inventory inventory = new Inventory();
        inventory.setSkuCode("Product_1");
        inventory.setQuantity(100);

        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("Product_2");
        inventory1.setQuantity(0);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory1);
    }
}
