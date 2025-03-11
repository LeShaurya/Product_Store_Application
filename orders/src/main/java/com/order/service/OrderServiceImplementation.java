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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class OrderServiceImplementation implements OrderService{

    private ProductProxy productProxy;
    private InventoryProxy inventoryProxy;
    private OrderRepository orderRepository;

    @Autowired
    KafkaTemplate<String, com.common.OrderDto> kafkaTemplate;

    private static final String TOPIC = "order-created";

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating order for SKU: {}, quantity: {}", orderDto.getSkuCode(), orderDto.getQuantity());

        try {
            log.debug("Fetching product information for SKU: {}", orderDto.getSkuCode());
            ProductDto product = productProxy.getProductBySkuCode(orderDto.getSkuCode());

            log.debug("Reserving inventory for SKU: {}, quantity: {}", orderDto.getSkuCode(), orderDto.getQuantity());
            InventoryUpdateDto inventoryUpdate = new InventoryUpdateDto(orderDto.getSkuCode(), orderDto.getQuantity());
            inventoryProxy.reserveInventory(inventoryUpdate);

            log.debug("Converting and saving order to database");
            Order order = OrderTypeConversion.convert(orderDto);
            order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
            orderRepository.save(order);

            orderDto.setProductName(product.getProductName());

            log.debug("Publishing order created event to Kafka topic: {}", TOPIC);
            com.common.OrderDto orderDto1 = OrderTypeConversion.convertToKafkaDto(orderDto);
            kafkaTemplate.send(TOPIC, orderDto1);

            log.info("Order successfully created with SKU: {}, product: {}",
                    orderDto.getSkuCode(), orderDto.getProductName());
            return orderDto;
        } catch (Exception e) {
            log.error("Failed to create order for SKU: {}", orderDto.getSkuCode(), e);
            throw e;
        }
    }
}