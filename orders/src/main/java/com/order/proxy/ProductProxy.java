package com.order.proxy;

import com.order.decoder.ProductServiceErrorDecoder;
import com.order.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "products", url = "http://localhost:8090/api/products", configuration = ProductServiceErrorDecoder.class)
public interface ProductProxy {

    @GetMapping("/{skuCode}")
    ProductDto getProductBySkuCode(@PathVariable String skuCode);

    @GetMapping("/exists/{skuCode}")
    boolean productExists(@PathVariable String skuCode);
}
