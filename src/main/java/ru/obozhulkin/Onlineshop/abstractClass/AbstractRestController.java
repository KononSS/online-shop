package ru.obozhulkin.Onlineshop.abstractClass;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;

@Slf4j
public class AbstractRestController {

    protected final ProductDetailsService productDetailsService;
    protected final PersonDetailsService personDetailsService;
    protected final ProductValidator productValidator;
    protected final ModelMapper modelMapper;

    @Autowired
    public AbstractRestController(ProductDetailsService productDetailsService, PersonDetailsService personDetailsService, ProductValidator productValidator, ModelMapper modelMapper) {
        this.productDetailsService = productDetailsService;
        this.personDetailsService = personDetailsService;
        this.productValidator = productValidator;
        this.modelMapper = modelMapper;
    }

    protected void logEndpointAccess(String endpoint) {
        log.info("Accessed {} endpoint", endpoint);
    }
}
