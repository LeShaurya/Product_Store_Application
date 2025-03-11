package com.inventory.service;

import com.inventory.dto.InventoryUpdateDto;
import com.inventory.exceptions.InsufficientInventoryException;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class InventoryServiceImplementation implements InventoryService {

    private InventoryRepository inventoryRepository;

    @Override
    public boolean reserve(InventoryUpdateDto inventoryUpdateDto) {
        log.info("Attempting to reserve inventory with SKU: {}, quantity: {}",
                inventoryUpdateDto.getSkuCode(), inventoryUpdateDto.getQuantity());

        boolean productInventoryExists = inventoryRepository.existsBySkuCodeAndAvailableQuantity(
                inventoryUpdateDto.getSkuCode(),
                inventoryUpdateDto.getQuantity()
        );

        if (productInventoryExists) {
            Inventory productInventory = inventoryRepository.findBySkuCode(inventoryUpdateDto.getSkuCode());
            int previousQuantity = productInventory.getQuantity();
            productInventory.setQuantity(previousQuantity - inventoryUpdateDto.getQuantity());
            inventoryRepository.save(productInventory);

            log.info("Successfully reserved inventory for SKU: {}. Previous quantity: {}, New quantity: {}",
                    inventoryUpdateDto.getSkuCode(), previousQuantity, productInventory.getQuantity());

            inventoryUpdateDto.setQuantity(productInventory.getQuantity());
            return true;
        }

        log.error("Insufficient inventory for SKU: {}, requested quantity: {}",
                inventoryUpdateDto.getSkuCode(), inventoryUpdateDto.getQuantity());
        throw new InsufficientInventoryException("Product inventory not sufficient");
    }

    @Override
    public boolean updateProductInventory(InventoryUpdateDto inventoryUpdateDto) {
        log.info("Attempting to update inventory for SKU: {} to quantity: {}",
                inventoryUpdateDto.getSkuCode(), inventoryUpdateDto.getQuantity());

        if(inventoryUpdateDto.getQuantity() >= 0) {
            Inventory productInventory = inventoryRepository.findBySkuCode(inventoryUpdateDto.getSkuCode());
            int previousQuantity = productInventory.getQuantity();
            productInventory.setQuantity(inventoryUpdateDto.getQuantity());
            inventoryRepository.save(productInventory);

            log.info("Successfully updated inventory for SKU: {}. Previous quantity: {}, New quantity: {}",
                    inventoryUpdateDto.getSkuCode(), previousQuantity, productInventory.getQuantity());
            return true;
        }

        log.error("Invalid quantity update request for SKU: {}, requested quantity: {}",
                inventoryUpdateDto.getSkuCode(), inventoryUpdateDto.getQuantity());
        throw new InsufficientInventoryException("available product inventory should not be negative.");
    }
}