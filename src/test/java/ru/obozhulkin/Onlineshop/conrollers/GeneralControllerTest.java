package ru.obozhulkin.Onlineshop.conrollers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.obozhulkin.Onlineshop.exeption.ProductNotFoundException;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.security.PersonDetails;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(GeneralController.class)
public class GeneralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductDetailsService productDetailsService;

    @MockBean
    private PersonDetailsService personDetailsService;

    @InjectMocks
    private GeneralController generalController;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSayHello() throws Exception {
        mockMvc.perform(get("/user/hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/hello"));
    }

    @Test
    public void testCatalog() throws Exception {
        when(productDetailsService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/showListProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testBasket() throws Exception {
        Person person = new Person();
        person.setId(1);
        when(personDetailsService.authenticatePerson()).thenReturn(new PersonDetails(person));
        when(personDetailsService.getProductByPersonId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/user/basket"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/basket"))
                .andExpect(model().attributeExists("basket"))
                .andExpect(model().attribute("totalSum", 0.0));
    }

    @Test
    public void testDelete() throws Exception {
        Person person = new Person();
        person.setId(1);
        when(personDetailsService.authenticatePerson()).thenReturn(new PersonDetails(person));

        mockMvc.perform(post("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/basket"));

        verify(personDetailsService, times(1)).deleteProduct(1, 1);
    }

    @Test
    public void testMakeSearch() throws Exception {
        when(productDetailsService.searchByTitle("test")).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/user/search")
                        .param("name", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/showListProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testShow() throws Exception {
        Product product = new Product();
        product.setProduct_id(1);
        when(productDetailsService.findOne(1)).thenReturn(product);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/showInfoProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testAddInBasketSuccess() throws Exception {
        Person person = new Person();
        person.setId(1);
        Product product = new Product();
        product.setProduct_id(1);
        product.setQuantity(1);
        when(personDetailsService.authenticatePerson()).thenReturn(new PersonDetails(person));
        when(productDetailsService.findOne(1)).thenReturn(product);

        mockMvc.perform(post("/user/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/1"));

        verify(personDetailsService, times(1)).addBasket(1, 1);
    }

    @Test
    public void testAddInBasketOutOfStock() throws Exception {
        Person person = new Person();
        person.setId(1);
        Product product = new Product();
        product.setProduct_id(1);
        product.setQuantity(0);
        when(personDetailsService.authenticatePerson()).thenReturn(new PersonDetails(person));
        when(productDetailsService.findOne(1)).thenReturn(product);

        mockMvc.perform(post("/user/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/1"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    public void testAddInBasketProductNotFound() throws Exception {
        when(productDetailsService.findOne(1)).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(post("/user/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/catalog"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    public void testAddInBasketException() throws Exception {
        when(productDetailsService.findOne(1)).thenThrow(new RuntimeException("An error occurred"));

        mockMvc.perform(post("/user/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/catalog"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}