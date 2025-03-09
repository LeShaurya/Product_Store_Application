package com.order.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.OrdersApplication;
import com.order.dto.OrderDto;
import com.order.dto.InventoryUpdateDto;
import com.order.dto.ProductDto;
import com.order.model.Order;
import com.order.proxy.InventoryProxy;
import com.order.proxy.ProductProxy;
import com.order.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrdersApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class OrderServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("order_service_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductProxy productProxy;

    @MockBean
    private InventoryProxy inventoryProxy;

    // Mock the KafkaTemplate to avoid actual Kafka interactions
    @MockBean
    private KafkaTemplate<String, com.common.OrderDto> kafkaTemplate;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    private OrderDto buildOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setSkuCode("SKU123");
        orderDto.setQuantity(2);
        orderDto.setCustomerName("John Doe");
        orderDto.setCustomerEmail("john.doe@example.com");
        orderDto.setCustomerPhone("1234567890");
        orderDto.setShippingAddress("123 Main St, City, Country");
        return orderDto;
    }

    private ProductDto buildProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setProductName("Test Product");
        productDto.setSkuCode("SKU123");
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setDescription("Test Description");
        return productDto;
    }

    private InventoryUpdateDto buildInventoryUpdateResult() {
        InventoryUpdateDto updateDto = new InventoryUpdateDto("SKU123", 2);
        // Set any additional fields that indicate successful reservation
        return updateDto;
    }

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    public void testCreateOrder_Success() throws Exception {
        // Arrange
        OrderDto orderDto = buildOrderDto();
        ProductDto productDto = buildProductDto();
        InventoryUpdateDto inventoryUpdateDto = buildInventoryUpdateResult();

        // Mock external service responses
        when(productProxy.getProductBySkuCode("SKU123")).thenReturn(productDto);
        when(inventoryProxy.reserveInventory(any(InventoryUpdateDto.class))).thenReturn(inventoryUpdateDto);
        
        // Mock KafkaTemplate to do nothing (we're not testing Kafka here)
        when(kafkaTemplate.send(any(), any())).thenReturn(null);

        // Act
        ResultActions response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        // Assert HTTP response
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.skuCode", is(orderDto.getSkuCode())))
                .andExpect(jsonPath("$.quantity", is(orderDto.getQuantity())))
                .andExpect(jsonPath("$.customerName", is(orderDto.getCustomerName())))
                .andExpect(jsonPath("$.productName", is(productDto.getProductName())));

        // Verify data was saved in the database
        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size());
        assertEquals(orderDto.getSkuCode(), orders.get(0).getSkuCode());
        assertEquals(orderDto.getQuantity(), orders.get(0).getQuantity());
        assertEquals(orderDto.getCustomerName(), orders.get(0).getCustomerName());
        assertNotNull(orders.get(0).getOrderDate());
    }
}