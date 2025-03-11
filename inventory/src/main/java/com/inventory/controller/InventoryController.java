package com.inventory.controller;

import com.inventory.dto.InventoryUpdateDto;
import com.inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/inventory")
@AllArgsConstructor
@CrossOrigin("*")
@Log4j2
public class InventoryController {
    private InventoryService inventoryService;

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.OK)
    public InventoryUpdateDto reserveInventory(@RequestBody InventoryUpdateDto inventoryUpdateDto) {
        log.info("Received request to reserve inventory: {}", inventoryUpdateDto);
        inventoryService.reserve(inventoryUpdateDto);
        log.info("Successfully reserved inventory for product ID: {}", inventoryUpdateDto.getSkuCode());
        return inventoryUpdateDto;
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public InventoryUpdateDto updateInventory(@RequestBody InventoryUpdateDto inventoryUpdateDto) {
        log.info("Received request to update inventory: {}", inventoryUpdateDto);
        inventoryService.updateProductInventory(inventoryUpdateDto);
        log.info("Successfully updated inventory for product ID: {}", inventoryUpdateDto.getSkuCode());
        return inventoryUpdateDto;
    }
}