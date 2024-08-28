package ru.obozhulkin.Onlineshop.conrollers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeneralPageController.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneralPageControllerTest {
    private static final String MOCK_STR = "test";
    @MockBean
    private PersonDetailsService mockPersonDetailsService;
    @MockBean
    private ProductDetailsService mockProductDetailsService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void sayHelloTest() {
        mockMvc.perform(get("/user/hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/hello"));
    }

    @Test
    @SneakyThrows
    void catalog() {
        mockMvc.perform(get("/user/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/showListProduct"))
                .andExpect(model().attributeExists("product"));
    }

//    @Test
//    @SneakyThrows
//    void basket() {
//        Authentication authentication = mock(Authentication.class);
//        mockMvc.perform(get("/user/basket"))
//
//                .andExpect(status().isOk())
//                .andExpect(view().name("user/basket"))
//                .andExpect(model().attributeExists("product"));
//    }

//    @Test
//    @SneakyThrows
//    void makeSearch() {
//        mockMvc.perform(get("/user/search"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("user/hello"));
//    }

//    @Test
//    @SneakyThrows
//    void show() {
//        mockMvc.perform(get("/user/{id}", 1))
//                .andExpect(status().isOk())
//                .andExpect(view().name("user/showInfoProduct"));
//    }

//    @Test
//    @SneakyThrows
//    void addInBasket() {
//        mockMvc.perform(get("/user/hello"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("user/hello"));
//    }
}