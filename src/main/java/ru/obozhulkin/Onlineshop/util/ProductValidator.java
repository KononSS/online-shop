package ru.obozhulkin.Onlineshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;

@Slf4j
@Component
public class ProductValidator implements Validator {

    private final ProductDetailsService productDetailsService;

    @Autowired
    public ProductValidator(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.equals(aClass);
    }

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
