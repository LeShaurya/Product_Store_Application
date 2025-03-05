package com.products.repository;

import com.products.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    Optional<Product> findBySkuCode(String skuCode);
}
