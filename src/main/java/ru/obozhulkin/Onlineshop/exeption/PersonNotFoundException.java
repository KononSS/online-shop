package ru.obozhulkin.Onlineshop.exeption;

import lombok.extern.slf4j.Slf4j;

/**
 * Исключение, выбрасываемое при отсутствии пользователя.
 */
@Slf4j
public class PersonNotFoundException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public PersonNotFoundException(String message) {
        super(message);
        log.error("Person not found: {}", message);
    }
}
