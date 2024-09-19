package ru.obozhulkin.Onlineshop.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.repositories.ProductRepository;
import ru.obozhulkin.Onlineshop.exeption.ProductNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductDetailsServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductDetailsService productDetailsService;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setProduct_id(1);
        product.setTitle("Test Product");
        product.setQuantity(10);
    }

    @Test
    public void testRegisterProduct() {
        productDetailsService.registerProduct(product, "admin");

        verify(productRepository, times(1)).save(product);
        assertNotNull(product.getCreatedAt());
        assertEquals("admin", product.getCreatedWho());
    }

    @Test
    public void testFindAll() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> products = productDetailsService.findAll();

        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testFindOneFound() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product foundProduct = productDetailsService.findOne(1);

        assertNotNull(foundProduct);
        assertEquals(product, foundProduct);
    }

    @Test
    public void testFindOneNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productDetailsService.findOne(1);
        });
    }

    @Test
    public void testDelete() {
        productDetailsService.delete(1);

        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testSearchByTitle() {
        when(productRepository.findByTitleStartingWith("Test")).thenReturn(Collections.singletonList(product));

        List<Product> products = productDetailsService.searchByTitle("Test");

        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testEnrichProduct() {
        productDetailsService.registerProduct(product, "admin");

        assertNotNull(product.getCreatedAt());
        assertEquals("admin", product.getCreatedWho());
    }
}