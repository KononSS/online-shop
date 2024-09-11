package ru.obozhulkin.Onlineshop.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
        log.error("Product not found: {}", message);
    }
}
