package com.inventory.repository;

import com.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM Inventory i WHERE i.skuCode = :skuCode AND i.quantity >= :quantity")
    boolean existsBySkuCodeAndAvailableQuantity(@Param("skuCode") String skuCode, @Param("quantity") int quantity);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM Inventory i WHERE i.skuCode = :skuCode")
    boolean existsBySkuCode(@Param("skuCode") String skuCode);

    Inventory findBySkuCode(String skuCode);
}
