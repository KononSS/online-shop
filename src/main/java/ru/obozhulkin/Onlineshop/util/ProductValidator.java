package ru.obozhulkin.Onlineshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;

/**
 * Валидатор для продуктов.
 */
@Slf4j
@Component
public class ProductValidator implements Validator {

    /** Сервис для работы с продуктами. */
    private final ProductDetailsService productDetailsService;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param productDetailsService Сервис для работы с продуктами.
     */
    @Autowired
    public ProductValidator(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    /**
     * Проверяет, поддерживается ли валидация для указанного класса.
     *
     * @param aClass Класс для проверки.
     * @return true, если валидация поддерживается, иначе false.
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.equals(aClass);
    }

    /**
     * Валидирует объект продукта.
     *
     * @param o Объект для валидации.
     * @param errors Объект для хранения ошибок валидации.
     */
    @Override
    public void validate(Object o, Errors errors) {
        Product product = (Product) o;
        log.debug("Validating product: {}", product);
        if (productDetailsService.title(product.getTitle()).isPresent()) {
            log.warn("Product with title {} already exists", product.getTitle());
            errors.rejectValue("title", "", "Такой товар уже зарегистрирован");
        }
        log.debug("Validation completed for product: {}", product);
    }
}
