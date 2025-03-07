package com.order.proxy;

import com.order.dto.InventoryUpdateDto;
import com.order.dto.ProductDto;
import com.order.exceptions.InsufficientInventoryException;
import com.order.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class InventoryProxyFallback implements InventoryProxy {

    @Override
    public InventoryUpdateDto reserveInventory(InventoryUpdateDto inventoryUpdateDto) {
        throw new InsufficientInventoryException("Inventory service down or insufficient quantity.");
    }
}
