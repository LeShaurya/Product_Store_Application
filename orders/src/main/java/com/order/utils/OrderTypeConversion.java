package com.order.utils;

import com.order.dto.OrderDto;
import com.order.model.Order;


public class OrderTypeConversion {
    public static OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setSkuCode(order.getSkuCode());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setCustomerName(order.getCustomerName());
        orderDto.setCustomerEmail(order.getCustomerEmail());
        orderDto.setCustomerPhone(order.getCustomerPhone());
        orderDto.setShippingAddress(order.getShippingAddress());
        return orderDto;
    }

    public static Order convert(OrderDto orderDto) {
        Order order = new Order();
        order.setSkuCode(orderDto.getSkuCode());
        order.setQuantity(orderDto.getQuantity());
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerEmail(orderDto.getCustomerEmail());
        order.setCustomerPhone(orderDto.getCustomerPhone());
        order.setShippingAddress(orderDto.getShippingAddress());
        return order;
    }

    public static com.common.OrderDto convertToKafkaDto(OrderDto orderDto) {
        com.common.OrderDto order = new com.common.OrderDto();
        order.setSkuCode(orderDto.getSkuCode());
        order.setProductName(orderDto.getProductName());
        order.setQuantity(orderDto.getQuantity());
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerEmail(orderDto.getCustomerEmail());
        order.setCustomerPhone(orderDto.getCustomerPhone());
        order.setShippingAddress(orderDto.getShippingAddress());
        return order;

    }

}
