package com.order.proxy;

import com.order.decoder.InventoryServiceErrorDecoder;
import com.order.dto.InventoryUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory", url = "http://localhost:8092/api/inventory", configuration = InventoryServiceErrorDecoder.class)
public interface InventoryProxy {
    @PostMapping("/reserve")
    InventoryUpdateDto reserveInventory(@RequestBody InventoryUpdateDto inventoryUpdateDto);
}
