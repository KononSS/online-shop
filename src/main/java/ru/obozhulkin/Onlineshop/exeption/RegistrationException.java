package ru.obozhulkin.Onlineshop.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegistrationException extends RuntimeException {

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
        log.error("Registration error: {}", message);
    }
}
