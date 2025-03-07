package com.order.controller;

import com.order.dto.ErrorDetails;
import com.order.exceptions.InsufficientInventoryException;
import com.order.exceptions.ProductNotFoundException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorMessage(ex.getMessage())
                .timestamp(LocalTime.now())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDetails> handleFeignException(FeignException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorMessage("Error communicating with the product service")
                .timestamp(LocalTime.now())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ErrorDetails> handleInventoryException(InsufficientInventoryException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .errorMessage(ex.getMessage())
                .timestamp(LocalTime.now())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}
