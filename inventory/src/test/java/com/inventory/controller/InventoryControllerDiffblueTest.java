package com.inventory.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.dto.InventoryUpdateDto;
import com.inventory.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {InventoryController.class, AppErrorHandler.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class InventoryControllerDiffblueTest {
    @Autowired
    private AppErrorHandler appErrorHandler;

    @Autowired
    private InventoryController inventoryController;

    @MockBean
    private InventoryService inventoryService;

    /**
     * Test {@link InventoryController#reserveInventory(InventoryUpdateDto)}.
     * <p>
     * Method under test: {@link InventoryController#reserveInventory(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test reserveInventory(InventoryUpdateDto)")
    @Tag("MaintainedByDiffblue")
    void testReserveInventory() throws Exception {
        // Arrange
        when(inventoryService.reserve(Mockito.<InventoryUpdateDto>any())).thenReturn(true);

        InventoryUpdateDto inventoryUpdateDto = new InventoryUpdateDto();
        inventoryUpdateDto.setQuantity(1);
        inventoryUpdateDto.setSkuCode("Sku Code");
        String content = (new ObjectMapper()).writeValueAsString(inventoryUpdateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/inventory/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"skuCode\":\"Sku Code\",\"quantity\":1}"));
    }

    /**
     * Test {@link InventoryController#updateInventory(InventoryUpdateDto)}.
     * <p>
     * Method under test: {@link InventoryController#updateInventory(InventoryUpdateDto)}
     */
    @Test
    @DisplayName("Test updateInventory(InventoryUpdateDto)")
    @Tag("MaintainedByDiffblue")
    void testUpdateInventory() throws Exception {
        // Arrange
        when(inventoryService.updateProductInventory(Mockito.<InventoryUpdateDto>any())).thenReturn(true);

        InventoryUpdateDto inventoryUpdateDto = new InventoryUpdateDto();
        inventoryUpdateDto.setQuantity(1);
        inventoryUpdateDto.setSkuCode("Sku Code");
        String content = (new ObjectMapper()).writeValueAsString(inventoryUpdateDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"skuCode\":\"Sku Code\",\"quantity\":1}"));
    }
}
