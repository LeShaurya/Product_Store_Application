package com.order.service;

import com.order.dto.InventoryUpdateDto;
import com.order.dto.OrderDto;
import com.order.dto.ProductDto;
import com.order.model.Order;
import com.order.proxy.InventoryProxy;
import com.order.proxy.ProductProxy;
import com.order.repository.OrderRepository;
import com.order.utils.OrderTypeConversion;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class OrderServiceImplementation implements OrderService{

    private ProductProxy productProxy;
    private InventoryProxy inventoryProxy;
    private OrderRepository orderRepository;

    @Autowired
    KafkaTemplate<String, com.common.OrderDto> kafkaTemplate;

    private static final String TOPIC = "order-created";

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        ProductDto product = productProxy.getProductBySkuCode(orderDto.getSkuCode());
        InventoryUpdateDto inventoryUpdate = new InventoryUpdateDto(orderDto.getSkuCode(), orderDto.getQuantity());
        inventoryProxy.reserveInventory(inventoryUpdate);
        Order order = OrderTypeConversion.convert(orderDto);
        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        orderRepository.save(order);
        orderDto.setProductName(product.getProductName());
        com.common.OrderDto orderDto1 = OrderTypeConversion.convertToKafkaDto(orderDto);
        kafkaTemplate.send(TOPIC, orderDto1);
        return orderDto;
    }
}
