package com.inventory.service;

import com.inventory.dto.InventoryUpdateDto;
import com.inventory.model.Inventory;

public interface InventoryService {
    public boolean reserve(InventoryUpdateDto inventoryUpdateDto);
    public boolean updateProductInventory(InventoryUpdateDto inventoryUpdateDto);
}
