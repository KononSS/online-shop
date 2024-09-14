package ru.obozhulkin.Onlineshop.exeption;

import lombok.extern.slf4j.Slf4j;

/**
 * Исключение, выбрасываемое при отсутствии продукта.
 */
@Slf4j
public class ProductNotFoundException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public ProductNotFoundException(String message) {
        super(message);
        log.error("Product not found: {}", message);
    }
}
