package com.order.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.OrderDto;
import com.order.exceptions.InsufficientInventoryException;
import com.order.exceptions.ProductNotFoundException;
import com.order.proxy.InventoryProxy;
import com.order.proxy.ProductProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceFailureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductProxy productProxy;

    @MockBean
    private InventoryProxy inventoryProxy;
    
    @MockBean
    private KafkaTemplate<String, com.common.OrderDto> kafkaTemplate;

    @Test
    public void testCreateOrder_ProductNotFound() throws Exception {
        // Arrange
        OrderDto orderDto = buildOrderDto();
        
        // Mock product service failure
        when(productProxy.getProductBySkuCode(anyString()))
                .thenThrow(new ProductNotFoundException("Product not found"));

        // Act & Assert
        ResultActions response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOrder_InsufficientInventory() throws Exception {
        // Arrange
        OrderDto orderDto = buildOrderDto();
        
        // Mock product service success but inventory service failure
        when(productProxy.getProductBySkuCode(anyString())).thenReturn(buildProductDto());
        when(inventoryProxy.reserveInventory(any()))
                .thenThrow(new InsufficientInventoryException("Insufficient inventory"));

        // Act & Assert
        ResultActions response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateOrder_ValidationFailure() throws Exception {
        // Arrange - create an invalid order DTO (missing required fields)
        OrderDto invalidOrderDto = new OrderDto();
        invalidOrderDto.setSkuCode("SKU123"); // Only set SKU, missing other required fields

        // Act & Assert
        ResultActions response = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidOrderDto)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
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

    private com.order.dto.ProductDto buildProductDto() {
        com.order.dto.ProductDto productDto = new com.order.dto.ProductDto();
        productDto.setProductName("Test Product");
        productDto.setSkuCode("SKU123");
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setDescription("Test Description");
        return productDto;
    }
}