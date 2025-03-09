package com.inventory.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.dto.InventoryUpdateDto;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Transactional
public class InventoryControllerIntegrationTest {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("inventory-test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryRepository inventoryRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setUp() {
        inventoryRepository.deleteAll();
        
        // Add test data
        Inventory product1 = new Inventory("PROD-001", 100);
        Inventory product2 = new Inventory("PROD-002", 50);
        Inventory product3 = new Inventory("PROD-003", 5);
        
        inventoryRepository.save(product1);
        inventoryRepository.save(product2);
        inventoryRepository.save(product3);
    }

    @Test
    public void testReserveInventorySuccessful() throws Exception {
        InventoryUpdateDto request = new InventoryUpdateDto();
        request.setSkuCode("PROD-001");
        request.setQuantity(10);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skuCode", equalTo("PROD-001")))
                .andExpect(jsonPath("$.quantity", equalTo(90))); // Original 100 - 10 = 90

        // Verify database was updated correctly
        Inventory updatedInventory = inventoryRepository.findBySkuCode("PROD-001");
        assertEquals(90, updatedInventory.getQuantity());
    }

    @Test
    public void testReserveInventoryInsufficientStock() throws Exception {
        InventoryUpdateDto request = new InventoryUpdateDto();
        request.setSkuCode("PROD-003");
        request.setQuantity(10); // Trying to reserve more than available (5)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()); // Assuming InsufficientInventoryException returns 500

        // Verify inventory remained unchanged
        Inventory updatedInventory = inventoryRepository.findBySkuCode("PROD-003");
        assertEquals(5, updatedInventory.getQuantity());
    }

    @Test
    public void testReserveNonExistentProduct() throws Exception {
        InventoryUpdateDto request = new InventoryUpdateDto();
        request.setSkuCode("NON-EXISTENT");
        request.setQuantity(10);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()); // Assuming your service throws an exception
    }

    @Test
    public void testUpdateInventorySuccessful() throws Exception {
        InventoryUpdateDto request = new InventoryUpdateDto();
        request.setSkuCode("PROD-002");
        request.setQuantity(75); // Update from 50 to 75

        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skuCode", equalTo("PROD-002")))
                .andExpect(jsonPath("$.quantity", equalTo(75)));

        // Verify database was updated correctly
        Inventory updatedInventory = inventoryRepository.findBySkuCode("PROD-002");
        assertEquals(75, updatedInventory.getQuantity());
    }

    @Test
    public void testUpdateInventoryWithNegativeQuantity() throws Exception {
        InventoryUpdateDto request = new InventoryUpdateDto();
        request.setSkuCode("PROD-001");
        request.setQuantity(-10); // Negative quantity not allowed

        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()); // Assuming InsufficientInventoryException returns 500

        // Verify inventory remained unchanged
        Inventory updatedInventory = inventoryRepository.findBySkuCode("PROD-001");
        assertEquals(100, updatedInventory.getQuantity());
    }
    
    @Test
    public void testUpdateNonExistentProduct() throws Exception {
        InventoryUpdateDto request = new InventoryUpdateDto();
        request.setSkuCode("NON-EXISTENT");
        request.setQuantity(50);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()); // Assuming a NullPointerException or similar
    }
}