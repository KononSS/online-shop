package ru.obozhulkin.Onlineshop.exeption;

import lombok.extern.slf4j.Slf4j;

/**
 * Исключение, выбрасываемое при ошибке регистрации.
 */
@Slf4j
public class RegistrationException extends RuntimeException {

/**
 * Конструктор с сообщением об ошибке и причиной.
 * @param message Сообщение об ошибке.
 * @param cause Объект ошибки.
 */
    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
        log.error("Registration error: {}", message);
    }
}
