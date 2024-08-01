package com.project.logiware.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.logiware.inventory.model.Inventory;
import com.project.logiware.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class InventoryApplicationTests {
	@Container
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine")
			.withDatabaseName("logiwareDB_order")
			.withUsername("postgres")
			.withPassword("postgres");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InventoryRepository inventoryRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	@Test
	void testIsInStock() throws Exception {
		boolean isInStock;
		Inventory inventory = inventoryRepository.findBySkuCode("SKU Code for Product 1").orElse(null);
		if (inventory != null) {
			isInStock = (inventory.getQuantity() > 0);
		} else {
			isInStock = false;
		}

		String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/SKU Code for Product 1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		boolean response = Boolean.parseBoolean(responseContent);
		Assertions.assertEquals(isInStock, response);
	}
}
