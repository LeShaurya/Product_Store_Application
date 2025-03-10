package com.products.controller;

import com.products.dto.ProductDto;
import com.products.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{skuCode}")
    public ProductDto getBySkuCode(@PathVariable String skuCode) {
        return productService.getBySkuName(skuCode);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PutMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@PathVariable String skuCode, @RequestBody ProductDto productDto) {
        return productService.updateProduct(skuCode, productDto);
    }

    @DeleteMapping("/{skuCode}")
    @ResponseStatus
    public void deleteProduct(@PathVariable String skuCode) {
        productService.deleteProduct(skuCode);
    }

    @GetMapping("/{skuCode}/exists")
    @ResponseStatus(HttpStatus.OK)
    public boolean productExists(@PathVariable String skuCode) {
        return productService.productExists(skuCode);
    }
}