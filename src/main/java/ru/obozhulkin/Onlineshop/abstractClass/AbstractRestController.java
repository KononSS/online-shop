package ru.obozhulkin.Onlineshop.abstractClass;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;

/**
 * Абстрактный контроллер, содержащий общие зависимости и методы для REST API.
 */
@Slf4j
public class AbstractRestController {

    /** Сервис для работы с продуктами. */
    protected final ProductDetailsService productDetailsService;
    /** Сервис для работы с пользователями. */
    protected final PersonDetailsService personDetailsService;
    /** Валидатор для продуктов. */
    protected final ProductValidator productValidator;
    /** Маппер для преобразования объектов. */
    protected final ModelMapper modelMapper;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param productDetailsService Сервис для работы с продуктами.
     * @param personDetailsService Сервис для работы с пользователями.
     * @param productValidator Валидатор для продуктов.
     * @param modelMapper Маппер для преобразования объектов.
     */
    @Autowired
    protected AbstractRestController(ProductDetailsService productDetailsService, PersonDetailsService personDetailsService, ProductValidator productValidator, ModelMapper modelMapper) {
        this.productDetailsService = productDetailsService;
        this.personDetailsService = personDetailsService;
        this.productValidator = productValidator;
        this.modelMapper = modelMapper;
    }

    /**
     * Логирует доступ к указанному эндпоинту.
     *
     * @param endpoint Эндпоинт, к которому осуществлен доступ.
     */
    protected void logEndpointAccess(String endpoint) {
        log.info("Accessed {} endpoint", endpoint);
    }
}
