package com.inventory.service;

import com.inventory.dto.InventoryUpdateDto;
import com.inventory.exceptions.InsufficientInventoryException;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class InventoryServiceImplementation implements InventoryService {

    private InventoryRepository inventoryRepository;

    @Override
    public boolean reserve(InventoryUpdateDto inventoryUpdateDto) {
        boolean productInventoryExists = inventoryRepository.existsBySkuCodeAndAvailableQuantity(inventoryUpdateDto.getSkuCode(), inventoryUpdateDto.getQuantity());
        if(productInventoryExists) {
            Inventory productInventory = inventoryRepository.findBySkuCode(inventoryUpdateDto.getSkuCode());
            inventoryUpdateDto.setQuantity(productInventory.getQuantity() - inventoryUpdateDto.getQuantity());
            return updateProductInventory(inventoryUpdateDto);
        }
        throw new InsufficientInventoryException("product inventory not sufficient");
    }

    @Override
    public boolean updateProductInventory(InventoryUpdateDto inventoryUpdateDto) {
        if(inventoryUpdateDto.getQuantity() >= 0) {
            Inventory productInventory = inventoryRepository.findBySkuCode(inventoryUpdateDto.getSkuCode());
            productInventory.setQuantity(inventoryUpdateDto.getQuantity());
            inventoryRepository.save(productInventory);
            return true;
        }
        throw new InsufficientInventoryException("available product inventory should not be negative.");
    }
}
