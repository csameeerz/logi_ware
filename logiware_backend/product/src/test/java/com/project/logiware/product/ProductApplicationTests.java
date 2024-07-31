package com.project.logiware.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.logiware.product.dto.ProductRequest;
import com.project.logiware.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void testCreateProduct() throws Exception {
		int productSize = productRepository.findAll().size();
		ProductRequest productRequest = getProductRequest();
		String json = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated());

		Assertions.assertEquals(productSize + 1, productRepository.findAll().size());
	}

	@Test
	void testGetAllProducts() throws Exception {
		int productSize = productRepository.findAll().size();

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(productSize));
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Test Product")
				.description("Test Product Description")
				.price(BigDecimal.valueOf(1.99))
				.build();
	}

}

