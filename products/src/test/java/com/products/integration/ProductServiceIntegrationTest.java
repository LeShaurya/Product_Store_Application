package com.products.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.dto.ProductDto;
import com.products.model.Product;
import com.products.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
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
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ProductServiceIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.9");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @AfterEach
    void cleanup() {
        productRepository.deleteAll();
    }

    private Product createTestProduct() {
        return new Product(
                "TEST-SKU-123",
                "Test Product",
                "Test Category",
                new BigDecimal("29.99"),
                "Test Vendor"
        );
    }

    private ProductDto createTestProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setSkuCode("TEST-SKU-123");
        productDto.setProductName("Test Product");
        productDto.setCategory("Test Category");
        productDto.setPrice(new BigDecimal("29.99"));
        productDto.setVendor("Test Vendor");
        return productDto;
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductDto productDto = createTestProductDto();
        String productDtoJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.skuCode", is(productDto.getSkuCode())))
                .andExpect(jsonPath("$.productName", is(productDto.getProductName())))
                .andExpect(jsonPath("$.category", is(productDto.getCategory())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice().doubleValue())))
                .andExpect(jsonPath("$.vendor", is(productDto.getVendor())));

        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size());
        assertEquals(productDto.getSkuCode(), products.get(0).getSkuCode());
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        // Arrange
        productRepository.save(createTestProduct());
        Product product2 = createTestProduct();
        product2.setSkuCode("TEST-SKU-456");
        product2.setProductName("Another Test Product");
        productRepository.save(product2);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].skuCode", is("TEST-SKU-123")))
                .andExpect(jsonPath("$[1].skuCode", is("TEST-SKU-456")));
    }

    @Test
    void shouldGetProductBySkuCode() throws Exception {
        // Arrange
        Product testProduct = createTestProduct();
        productRepository.save(testProduct);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{skuCode}", testProduct.getSkuCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skuCode", is(testProduct.getSkuCode())))
                .andExpect(jsonPath("$.productName", is(testProduct.getProductName())))
                .andExpect(jsonPath("$.category", is(testProduct.getCategory())))
                .andExpect(jsonPath("$.price", is(testProduct.getPrice().doubleValue())))
                .andExpect(jsonPath("$.vendor", is(testProduct.getVendor())));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        // Arrange
        Product testProduct = createTestProduct();
        productRepository.save(testProduct);

        ProductDto updatedProductDto = createTestProductDto();
        updatedProductDto.setProductName("Updated Product Name");
        updatedProductDto.setPrice(new BigDecimal("39.99"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{skuCode}", testProduct.getSkuCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skuCode", is(testProduct.getSkuCode())))
                .andExpect(jsonPath("$.productName", is("Updated Product Name")))
                .andExpect(jsonPath("$.price", is(39.99)));

        Product updatedProduct = productRepository.findBySkuCode(testProduct.getSkuCode()).orElseThrow();
        assertEquals("Updated Product Name", updatedProduct.getProductName());
        assertEquals(0, updatedProduct.getPrice().compareTo(new BigDecimal("39.99")));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        // Arrange
        Product testProduct = createTestProduct();
        productRepository.save(testProduct);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{skuCode}", testProduct.getSkuCode()))
                .andExpect(status().isOk());

        assertFalse(productRepository.findBySkuCode(testProduct.getSkuCode()).isPresent());
    }

    @Test
    void shouldCheckIfProductExists() throws Exception {
        // Arrange
        Product testProduct = createTestProduct();
        productRepository.save(testProduct);

        // Act & Assert - Product exists
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{skuCode}/exists", testProduct.getSkuCode()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Act & Assert - Product doesn't exist
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{skuCode}/exists", "NON-EXISTENT-SKU"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/NON-EXISTENT-SKU"))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/NON-EXISTENT-SKU")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTestProductDto())))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/NON-EXISTENT-SKU"))
                .andExpect(status().isNotFound());
    }
}