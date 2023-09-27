package com.khrystoforov.onlineshopapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductQuantityException extends RuntimeException {

    public ProductQuantityException(String message) {
        super(message);
    }
}
