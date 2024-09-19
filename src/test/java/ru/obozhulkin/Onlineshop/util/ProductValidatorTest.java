package ru.obozhulkin.Onlineshop.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductValidatorTest {

    @Mock
    private ProductDetailsService productDetailsService;

    @Mock
    private Errors errors;

    @InjectMocks
    private ProductValidator productValidator;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setTitle("Test Product");
    }

    @Test
    public void testSupports() {
        assertTrue(productValidator.supports(Product.class));
        assertFalse(productValidator.supports(Object.class));
    }

    @Test
    public void testValidateWithExistingTitle() {
        when(productDetailsService.title("Test Product")).thenReturn(Optional.of(product));

        productValidator.validate(product, errors);

        verify(errors, times(1)).rejectValue("title", "", "Такой товар уже зарегистрирован");
    }

    @Test
    public void testValidateWithNonExistingTitle() {
        when(productDetailsService.title("Test Product")).thenReturn(Optional.empty());

        productValidator.validate(product, errors);

        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
    }
}