package com.order.proxy;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.order.dto.InventoryUpdateDto;
import com.order.exceptions.InsufficientInventoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {InventoryProxyFallback.class})
@ExtendWith(SpringExtension.class)
class InventoryProxyFallbackTest {
    @Autowired
    private InventoryProxyFallback inventoryProxyFallback;

    /**
     * Test {@link InventoryProxyFallback#reserveInventory(InventoryUpdateDto)}.
     * <p>
     * Method under test: {@link InventoryProxyFallback#reserveInventory(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test reserveInventory(InventoryUpdateDto)")
    void testReserveInventory() {
        // Arrange, Act and Assert
        assertThrows(InsufficientInventoryException.class,
                () -> inventoryProxyFallback.reserveInventory(new InventoryUpdateDto("Sku Code", 1)));
    }
}
