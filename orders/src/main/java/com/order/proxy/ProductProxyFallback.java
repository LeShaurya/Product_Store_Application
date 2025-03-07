package com.order.proxy;

import com.order.dto.ProductDto;
import com.order.exceptions.ProductNotFoundException;
import com.order.proxy.ProductProxy;
import org.springframework.stereotype.Component;

@Component
public class ProductProxyFallback implements ProductProxy {

    @Override
    public ProductDto getProductBySkuCode(String skuCode) {
        throw new ProductNotFoundException("Product service is down or product not found.");
    }

    @Override
    public boolean productExists(String skuCode) {
        return false;
    }
}
