package com.products.controller;

import com.products.dto.ProductDto;
import com.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
@CrossOrigin("*")
@Log4j2
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProducts() {
        log.info("Request received to get all products");
        return productService.getAllProducts();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{skuCode}")
    public ProductDto getBySkuCode(@PathVariable String skuCode) {
        log.info("Request received to get product with SKU: {}", skuCode);
        return productService.getBySkuName(skuCode);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        log.info("Request received to create product: {}", productDto.getProductName());
        return productService.createProduct(productDto);
    }

    @PutMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@PathVariable String skuCode, @RequestBody ProductDto productDto) {
        log.info("Request received to update product with SKU: {}", skuCode);
        return productService.updateProduct(skuCode, productDto);
    }

    @DeleteMapping("/{skuCode}")
    @ResponseStatus
    public void deleteProduct(@PathVariable String skuCode) {
        log.info("Request received to delete product with SKU: {}", skuCode);
        productService.deleteProduct(skuCode);
    }

    @GetMapping("/{skuCode}/exists")
    @ResponseStatus(HttpStatus.OK)
    public boolean productExists(@PathVariable String skuCode) {
        log.info("Checking if product with SKU: {} exists", skuCode);
        return productService.productExists(skuCode);
    }
}