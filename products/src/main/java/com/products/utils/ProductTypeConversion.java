package com.products.utils;

import com.products.dto.ProductDto;
import com.products.model.Product;

public class ProductTypeConversion {
    public static ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setSkuCode(product.getSkuCode());
        productDto.setProductName(product.getProductName());
        productDto.setCategory(product.getCategory());
        productDto.setPrice(product.getPrice());
        productDto.setVendor(product.getVendor());
        return productDto;
    }

    public static Product convert(ProductDto productDto) {
        Product product = new Product();
        product.setSkuCode(productDto.getSkuCode());
        product.setProductName(productDto.getProductName());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setVendor(productDto.getVendor());
        return product;
    }
}
