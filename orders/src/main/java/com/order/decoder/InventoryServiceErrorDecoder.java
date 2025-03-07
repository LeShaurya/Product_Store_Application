package com.order.decoder;

import com.order.exceptions.InsufficientInventoryException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class InventoryServiceErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 404 -> new InsufficientInventoryException("Insufficient inventory available");
            case 409 -> new InsufficientInventoryException("Conflict while processing inventory request");
            default -> new Exception("Generic error from inventory service");
        };
    }
}
