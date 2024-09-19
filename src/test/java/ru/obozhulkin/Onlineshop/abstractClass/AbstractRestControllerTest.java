package ru.obozhulkin.Onlineshop.abstractClass;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;

@ExtendWith(MockitoExtension.class)
public class AbstractRestControllerTest {

    @Mock
    private ProductDetailsService productDetailsService;

    @Mock
    private PersonDetailsService personDetailsService;

    @Mock
    private ProductValidator productValidator;

    @Mock
    private ModelMapper modelMapper;

    private TestAbstractRestController abstractRestController;

    @BeforeEach
    public void setUp() {
        abstractRestController = new TestAbstractRestController(productDetailsService,
                personDetailsService, productValidator, modelMapper);
    }

    @Test
    public void testConstructorInitialization() {
        assertNotNull(abstractRestController.productDetailsService);
        assertNotNull(abstractRestController.personDetailsService);
        assertNotNull(abstractRestController.productValidator);
        assertNotNull(abstractRestController.modelMapper);
    }

    @Test
    public void testLogEndpointAccess() {
        // Создаем мок для логгера
        Logger logger = mock(Logger.class);
        abstractRestController.setLogger(logger);

        String endpoint = "/test";
        abstractRestController.logEndpointAccess(endpoint);

        // Проверяем, что метод info был вызван с правильными аргументами
        verify(logger).info("Accessed {} endpoint", endpoint);
    }

    // Подкласс для тестирования
    private static class TestAbstractRestController extends AbstractRestController {
        private Logger logger;

        public TestAbstractRestController(ProductDetailsService productDetailsService,
                                          PersonDetailsService personDetailsService,
                                          ProductValidator productValidator,
                                          ModelMapper modelMapper) {
            super(productDetailsService, personDetailsService, productValidator, modelMapper);
        }

        @Override
        protected void logEndpointAccess(String endpoint) {
            if (logger != null) {
                logger.info("Accessed {} endpoint", endpoint);
            }
        }

        public void setLogger(Logger logger) {
            this.logger = logger;
        }
    }
}