package com.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
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
    private String skuCode;
    private String productName;
    private String category;
    private BigDecimal price;
    private String vendor;

    public Product(String skuCode, String productName, String category, BigDecimal price, String vendor) {
        this.skuCode = skuCode;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.vendor = vendor;
    }
}
