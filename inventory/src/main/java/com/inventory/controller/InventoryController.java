package com.inventory.controller;

import com.inventory.dto.InventoryUpdateDto;
import com.inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/inventory")
@AllArgsConstructor
public class InventoryController {
    private InventoryService inventoryService;

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.OK)
    public InventoryUpdateDto reserveInventory(@RequestBody InventoryUpdateDto inventoryUpdateDto) {
        inventoryService.reserve(inventoryUpdateDto);
        return inventoryUpdateDto;
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public InventoryUpdateDto updateInventory(@RequestBody InventoryUpdateDto inventoryUpdateDto) {
        inventoryService.updateProductInventory(inventoryUpdateDto);
        return inventoryUpdateDto;
    }
}
