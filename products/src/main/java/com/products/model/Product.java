package com.products.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NotNull(message = "product sku-code should not be empty.")
    @NotBlank(message = "product sku-code should not be blank.")
    private String skuCode;
    @NotEmpty(message = "product name should not be empty")
    private String productName;
    @NotEmpty(message = "product category should not be empty")
    private String category;
    @NotNull(message = "product price should not be empty")
    @Range(min = 0, message = "Product price should be within range.")
    private BigDecimal price;
    @NotEmpty(message = "product vendor should not be empty")
    private String vendor;

    public Product(String skuCode, String productName, String category, BigDecimal price, String vendor) {
        this.skuCode = skuCode;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.vendor = vendor;
    }
}
