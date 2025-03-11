package com.inventory.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.inventory.dto.InventoryUpdateDto;
import com.inventory.exceptions.InsufficientInventoryException;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {InventoryServiceImplementation.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class InventoryServiceImplementationTest {
    @MockBean
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryServiceImplementation inventoryServiceImplementation;

    /**
     * Test {@link InventoryServiceImplementation#reserve(InventoryUpdateDto)}.
     * <p>
     * Method under test: {@link InventoryServiceImplementation#reserve(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test reserve(InventoryUpdateDto)")
    @MethodsUnderTest({"boolean InventoryServiceImplementation.reserve(InventoryUpdateDto)"})
    void testReserve() {
        // Arrange
        when(inventoryRepository.existsBySkuCodeAndAvailableQuantity(Mockito.<String>any(), anyInt())).thenReturn(false);

        // Act and Assert
        assertThrows(InsufficientInventoryException.class,
                () -> inventoryServiceImplementation.reserve(new InventoryUpdateDto("Sku Code", 1)));
        verify(inventoryRepository).existsBySkuCodeAndAvailableQuantity(eq("Sku Code"), eq(1));
    }

    /**
     * Test {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}.
     * <p>
     * Method under test: {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test updateProductInventory(InventoryUpdateDto)")
    @MethodsUnderTest({"boolean InventoryServiceImplementation.updateProductInventory(InventoryUpdateDto)"})
    void testUpdateProductInventory() {
        // Arrange
        Inventory inventory = new Inventory();
        inventory.setQuantity(1);
        inventory.setSkuCode("Sku Code");
        when(inventoryRepository.save(Mockito.<Inventory>any()))
                .thenThrow(new InsufficientInventoryException("An error occurred"));
        when(inventoryRepository.findBySkuCode(Mockito.<String>any())).thenReturn(inventory);

        // Act and Assert
        assertThrows(InsufficientInventoryException.class,
                () -> inventoryServiceImplementation.updateProductInventory(new InventoryUpdateDto("Sku Code", 1)));
        verify(inventoryRepository).findBySkuCode(eq("Sku Code"));
        verify(inventoryRepository).save(isA(Inventory.class));
    }

    /**
     * Test {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}.
     * <p>
     * Method under test: {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test updateProductInventory(InventoryUpdateDto)")
    @MethodsUnderTest({"boolean InventoryServiceImplementation.updateProductInventory(InventoryUpdateDto)"})
    void testUpdateProductInventory2() {
        // Arrange
        when(inventoryRepository.findBySkuCode(Mockito.<String>any()))
                .thenThrow(new InsufficientInventoryException("An error occurred"));

        InventoryUpdateDto inventoryUpdateDto = new InventoryUpdateDto("Sku Code", 1);
        inventoryUpdateDto.setQuantity(0);

        // Act and Assert
        assertThrows(InsufficientInventoryException.class,
                () -> inventoryServiceImplementation.updateProductInventory(inventoryUpdateDto));
        verify(inventoryRepository).findBySkuCode(eq("Sku Code"));
    }

    /**
     * Test {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}.
     * <ul>
     *   <li>Given {@link InventoryRepository}.</li>
     * </ul>
     * <p>
     * Method under test: {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test updateProductInventory(InventoryUpdateDto); given InventoryRepository")
    @MethodsUnderTest({"boolean InventoryServiceImplementation.updateProductInventory(InventoryUpdateDto)"})
    void testUpdateProductInventory_givenInventoryRepository() {
        // Arrange
        InventoryUpdateDto inventoryUpdateDto = new InventoryUpdateDto("Sku Code", 1);
        inventoryUpdateDto.setQuantity(-1);

        // Act and Assert
        assertThrows(InsufficientInventoryException.class,
                () -> inventoryServiceImplementation.updateProductInventory(inventoryUpdateDto));
    }

    /**
     * Test {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}.
     * <ul>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test: {@link InventoryServiceImplementation#updateProductInventory(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test updateProductInventory(InventoryUpdateDto); then return 'true'")
    @MethodsUnderTest({"boolean InventoryServiceImplementation.updateProductInventory(InventoryUpdateDto)"})
    void testUpdateProductInventory_thenReturnTrue() {
        // Arrange
        Inventory inventory = new Inventory();
        inventory.setQuantity(1);
        inventory.setSkuCode("Sku Code");

        Inventory inventory2 = new Inventory();
        inventory2.setQuantity(1);
        inventory2.setSkuCode("Sku Code");
        when(inventoryRepository.save(Mockito.<Inventory>any())).thenReturn(inventory2);
        when(inventoryRepository.findBySkuCode(Mockito.<String>any())).thenReturn(inventory);

        boolean actualUpdateProductInventoryResult = inventoryServiceImplementation
                .updateProductInventory(new InventoryUpdateDto("Sku Code", 1));

        verify(inventoryRepository).findBySkuCode(eq("Sku Code"));
        verify(inventoryRepository).save(isA(Inventory.class));
        assertTrue(actualUpdateProductInventoryResult);
    }
}
