package com.order.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.OrderDto;
import com.order.service.OrderServiceImplementation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {OrderController.class, GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class OrderControllerDiffblueTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private OrderController orderController;

    @MockBean
    private OrderServiceImplementation orderServiceImplementation;

    /**
     * Test {@link OrderController#createOrder(OrderDto)}.
     * <p>
     * Method under test: {@link OrderController#createOrder(OrderDto)}
     */
    @Test
    @DisplayName("Test createOrder(OrderDto)")
    @Tag("MaintainedByDiffblue")
    void testCreateOrder() throws Exception {
        // Arrange
        when(orderServiceImplementation.createOrder(Mockito.<OrderDto>any())).thenReturn(new OrderDto());

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerEmail("jane.doe@example.org");
        orderDto.setCustomerName("Customer Name");
        orderDto.setCustomerPhone("6625550144");
        orderDto.setProductName("Product Name");
        orderDto.setQuantity(1);
        orderDto.setShippingAddress("42 Main St");
        orderDto.setSkuCode("Sku Code");
        String content = (new ObjectMapper()).writeValueAsString(orderDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"skuCode\":null,\"productName\":null,\"quantity\":null,\"customerName\":null,\"customerEmail\":null,\"customerPhone"
                                        + "\":null,\"shippingAddress\":null}"));
    }
}
