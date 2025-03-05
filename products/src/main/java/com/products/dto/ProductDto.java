package com.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String skuCode;
    private String productName;
    private String category;
    private BigDecimal price;
    private String vendor;

}
