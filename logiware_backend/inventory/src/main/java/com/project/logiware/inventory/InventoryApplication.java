package com.project.logiware.inventory;

import com.project.logiware.inventory.model.Inventory;
import com.project.logiware.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
//		return args -> {
//			Inventory inventory1 = new Inventory();
//			inventory1.setSkuCode("SKU Code for Product 1");
//			inventory1.setQuantity(100);
//			inventoryRepository.save(inventory1);
//
//			Inventory inventory2 = new Inventory();
//			inventory2.setSkuCode("SKU Code for Product 2");
//			inventory2.setQuantity(0);
//			inventoryRepository.save(inventory2);
//
//			Inventory inventory3 = new Inventory();
//			inventory3.setSkuCode("SKU Code for Product 3");
//			inventory3.setQuantity(3);
//			inventoryRepository.save(inventory3);
//		};
//	}
}
