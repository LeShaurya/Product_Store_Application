package com.products.service;

import com.products.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getBySkuName(String skuName);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(String skuCode, ProductDto productDto);
    void deleteProduct(String skuCode);
    boolean productExists(String skuCode);
}
