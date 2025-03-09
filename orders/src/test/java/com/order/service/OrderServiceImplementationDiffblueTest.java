package com.order.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.order.dto.InventoryUpdateDto;
import com.order.dto.OrderDto;
import com.order.dto.ProductDto;
import com.order.model.Order;
import com.order.proxy.InventoryProxy;
import com.order.proxy.ProductProxy;
import com.order.repository.OrderRepository;

import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderServiceImplementation.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class OrderServiceImplementationDiffblueTest {
    @MockBean
    private InventoryProxy inventoryProxy;

    @MockBean
    private KafkaTemplate<String, com.common.OrderDto> kafkaTemplate;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderServiceImplementation orderServiceImplementation;

    @MockBean
    private ProductProxy productProxy;

    /**
     * Test {@link OrderServiceImplementation#createOrder(OrderDto)}.
     * <ul>
     *   <li>Given {@link KafkaTemplate} {@link KafkaTemplate#send(String, Object)} return {@link CompletableFuture#CompletableFuture()}.</li>
     *   <li>Then return {@link OrderDto#OrderDto()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link OrderServiceImplementation#createOrder(OrderDto)}
     */
    @Test
    @DisplayName("Test createOrder(OrderDto); given KafkaTemplate send(String, Object) return CompletableFuture(); then return OrderDto()")
    @Tag("MaintainedByDiffblue")
    void testCreateOrder_givenKafkaTemplateSendReturnCompletableFuture_thenReturnOrderDto() {
        // Arrange
        when(kafkaTemplate.send(Mockito.<String>any(), Mockito.<com.common.OrderDto>any()))
                .thenReturn(new CompletableFuture<>());
        when(productProxy.getProductBySkuCode(Mockito.<String>any())).thenReturn(new ProductDto());
        when(inventoryProxy.reserveInventory(Mockito.<InventoryUpdateDto>any()))
                .thenReturn(new InventoryUpdateDto("Sku Code", 1));

        Order order = new Order();
        order.setCustomerEmail("jane.doe@example.org");
        order.setCustomerName("Customer Name");
        order.setCustomerPhone("6625550144");
        order.setId(1L);
        order.setOrderDate(mock(Timestamp.class));
        order.setQuantity(1);
        order.setShippingAddress("42 Main St");
        order.setSkuCode("Sku Code");
        when(orderRepository.save(Mockito.<Order>any())).thenReturn(order);

        OrderDto orderDto = new OrderDto();
        orderDto.setQuantity(1);

        // Act
        OrderDto actualCreateOrderResult = orderServiceImplementation.createOrder(orderDto);

        // Assert
        verify(inventoryProxy).reserveInventory(isA(InventoryUpdateDto.class));
        verify(productProxy).getProductBySkuCode(isNull());
        verify(orderRepository).save(isA(Order.class));
        verify(kafkaTemplate).send(eq("order-created"), isA(com.common.OrderDto.class));
        assertSame(orderDto, actualCreateOrderResult);
    }
}
