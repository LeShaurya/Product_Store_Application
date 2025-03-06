package com.inventory.controller;

import com.inventory.dto.ErrorDetails;
import com.inventory.exceptions.InsufficientInventoryException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppErrorHandler {

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ErrorDetails> handleNotFound(InsufficientInventoryException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                                    .errorMessage(ex.getMessage())
                                    .timestamp(LocalTime.now())
                                    .errorCode(404)
                                    .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handle500(Exception ex){
        ErrorDetails errorDetails=  ErrorDetails.builder()
                                    .errorCode(500)
                                    .timestamp(LocalTime.now())
                                    .errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handle400(MethodArgumentNotValidException ex){

        String errorMessage= ex.getBindingResult()
                            .getAllErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining(", "));

        ErrorDetails errorDetails= ErrorDetails.builder()
                                    .errorCode(400)
                                    .timestamp(LocalTime.now())
                                    .errorMessage(errorMessage).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
}