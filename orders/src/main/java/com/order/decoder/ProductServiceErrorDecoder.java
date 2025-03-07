package com.order.decoder;

import com.order.exceptions.BadRequestException;
import com.order.exceptions.ProductNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 404 -> new ProductNotFoundException("Product not found in the product service");
            case 400 -> new BadRequestException("Bad request to product service");
            default -> new Exception("Generic error with the product service");
        };
    }
}
