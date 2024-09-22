package ru.obozhulkin.Onlineshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.obozhulkin.Onlineshop.models.Category;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;

/**
 * Валидатор для категорий.
 */
@Slf4j
@Component
public class CategoryValidator implements Validator {

    /** Сервис для работы с категориями. */
    private final ProductDetailsService productDetailsService;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param productDetailsService Сервис для работы с продуктами.
     */
    @Autowired
    public CategoryValidator(ProductDetailsService productDetailsService) {
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
        return Category.class.equals(aClass);
    }

    /**
     * Валидирует объект категории.
     *
     * @param o Объект для валидации.
     * @param errors Объект для хранения ошибок валидации.
     */
    @Override
    public void validate(Object o, Errors errors) {
        Category category = (Category) o;
        log.debug("Validating category: {}", category);
        if (productDetailsService.categoryName(category.getName()).isPresent()) {
            log.warn("Category with name {} already exists", category.getName());
            errors.rejectValue("name", "", "Такая категория уже зарегистрирована");
        }
        log.debug("Validation completed for category: {}", category);
    }
}