package com.project.logiware.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.logiware.order.dto.OrderLineItemsDto;
import com.project.logiware.order.dto.OrderRequest;
import com.project.logiware.order.repository.OrderRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class OrderApplicationTests {

	@Container
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine")
			.withDatabaseName("logiwareDB_inventory")
			.withUsername("postgres")
			.withPassword("postgres");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderRepository orderRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	@Test
	void testPlaceOrder() throws Exception {
		int orderSize = orderRepository.findAll().size();
		OrderRequest orderRequest = getOrderRequest();
		String json = objectMapper.writeValueAsString(orderRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isCreated());

		Assertions.assertEquals(orderSize + 1, orderRepository.findAll().size());
	}

	@Test
	void testGetAllProducts() throws Exception {
		int orderSize = orderRepository.findAll().size();

		mockMvc.perform(MockMvcRequestBuilders.get("/api/order")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(orderSize));
	}

	private OrderRequest getOrderRequest() {
		OrderRequest orderRequest = new OrderRequest();
		OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
		List<OrderLineItemsDto> orderLineItemsDtoList = new ArrayList<OrderLineItemsDto>();

		orderLineItemsDto.setQuantity(5);
		orderLineItemsDto.setPrice(BigDecimal.valueOf(49.99));
		orderLineItemsDto.setSkuCode("Test Order");

		orderLineItemsDtoList.add(orderLineItemsDto);
		orderRequest.setOrderLineItemsDtoList(orderLineItemsDtoList);

		return orderRequest;
	}
}
