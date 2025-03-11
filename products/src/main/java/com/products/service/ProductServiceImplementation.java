package com.products.service;

import com.products.dto.ProductDto;
import com.products.exceptions.ProductNotFoundException;
import com.products.model.Product;
import com.products.repository.ProductRepository;
import com.products.utils.ProductTypeConversion;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImplementation implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream().map(ProductTypeConversion::convertToDto).toList();
    }

    @Override
    public ProductDto getBySkuName(String skuName) {
        log.info("Fetching product with SKU: {}", skuName);
        Product productBySkuCode = productRepository.findBySkuCode(skuName).orElseThrow(() -> {
            log.error("Product with SKU: {} not found", skuName);
            return new ProductNotFoundException("Product not found");
        });
        log.info("Found product: {}", productBySkuCode.getProductName());
        return ProductTypeConversion.convertToDto(productBySkuCode);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Creating new product: {}, SKU: {}", productDto.getProductName(), productDto.getSkuCode());
        Product product = ProductTypeConversion.convert(productDto);
        log.debug("Converting DTO to entity: {}", product);
        Product savedProduct = productRepository.save(product);
        log.info("Product saved successfully with ID: {}", savedProduct.getId());
        return ProductTypeConversion.convertToDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(String skuCode, ProductDto productDto) {
        log.info("Updating product with SKU: {}", skuCode);
        Product existingProduct = productRepository.findBySkuCode(skuCode).orElseThrow(()-> {
            log.error("Product with SKU: {} not found for update", skuCode);
            return new ProductNotFoundException("Product not found");
        });
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setVendor(productDto.getVendor());
        log.debug("Updated product details: name={}, category={}, price={}, vendor={}",
                productDto.getProductName(), productDto.getCategory(),
                productDto.getPrice(), productDto.getVendor());
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product with SKU: {} updated successfully", skuCode);
        return ProductTypeConversion.convertToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(String skuCode) {
        log.info("Deleting product with SKU: {}", skuCode);
        Product product = productRepository.findBySkuCode(skuCode).orElseThrow(()-> {
            log.error("Product with SKU: {} not found for deletion", skuCode);
            return new ProductNotFoundException("Product not found");
        });
        log.debug("Found product to delete: {}", product.getProductName());
        productRepository.delete(product);
        log.info("Product with SKU: {} deleted successfully", skuCode);
    }

    @Override
    public boolean productExists(String skuCode) {
        log.debug("Checking if product with SKU: {} exists", skuCode);
        boolean exists = productRepository.findBySkuCode(skuCode).isPresent();
        log.debug("Product with SKU: {} exists: {}", skuCode, exists);
        return exists;
    }
}
