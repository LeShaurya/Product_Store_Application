package com.products.service;

import com.products.dto.ProductDto;
import com.products.exceptions.ProductNotFoundException;
import com.products.model.Product;
import com.products.repository.ProductRepository;
import com.products.utils.ProductTypeConversion;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImplementation implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(ProductTypeConversion::convertToDto).toList();
    }

    @Override
    public ProductDto getBySkuName(String skuName) {
        Product productBySkuCode = productRepository.findBySkuCode(skuName).orElseThrow(() ->new ProductNotFoundException("Product not found"));
        return ProductTypeConversion.convertToDto(productBySkuCode);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductTypeConversion.convert(productDto);

        Product savedProduct = productRepository.save(product);

        return ProductTypeConversion.convertToDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(String skuCode, ProductDto productDto) {
        Product existingProduct = productRepository.findBySkuCode(skuCode).orElseThrow(()->new ProductNotFoundException("Product not found"));
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setVendor(productDto.getVendor());

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductTypeConversion.convertToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(String skuCode) {
        Product product = productRepository.findBySkuCode(skuCode).orElseThrow(()->new ProductNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public boolean productExists(String skuCode) {
        return productRepository.findBySkuCode(skuCode).isPresent();
    }
}
