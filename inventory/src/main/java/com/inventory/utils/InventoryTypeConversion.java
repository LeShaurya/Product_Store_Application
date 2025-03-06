package com.inventory.utils;

import com.inventory.dto.InventoryUpdateDto;
import com.inventory.model.Inventory;

public class InventoryTypeConversion {
    public static InventoryUpdateDto convertToDto(Inventory inventory) {
        InventoryUpdateDto inventoryDto = new InventoryUpdateDto();
        inventoryDto.setSkuCode(inventory.getSkuCode());
        inventoryDto.setQuantity(inventory.getQuantity());
        return inventoryDto;
    }

    public static Inventory convert(InventoryUpdateDto inventoryDto) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryDto.getSkuCode());
        inventory.setQuantity(inventoryDto.getQuantity());
        return inventory;
    }
}
