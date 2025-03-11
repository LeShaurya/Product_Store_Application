package com.order.controller;

import com.order.dto.OrderDto;
import com.order.service.OrderServiceImplementation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
@CrossOrigin("*")
@Log4j2
public class OrderController {

    private final OrderServiceImplementation orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("Received request to create new order for product: {}", orderDto.getProductName());
        OrderDto createdOrder = orderService.createOrder(orderDto);
        log.info("Successfully created order with ID: {}", createdOrder.getSkuCode());
        return createdOrder;
    }
}