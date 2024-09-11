package ru.obozhulkin.Onlineshop.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String message) {
        super(message);
        log.error("Person not found: {}", message);
    }
}
