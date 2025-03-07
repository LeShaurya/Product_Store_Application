package com.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String skuCode;
    private String productName;
    private Integer quantity;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;

    public OrderDto(String skuCode, Integer quantity, String customerName, String customerEmail, String customerPhone, String shippingAddress) {
        this.skuCode = skuCode;
        this.quantity = quantity;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
    }
}
