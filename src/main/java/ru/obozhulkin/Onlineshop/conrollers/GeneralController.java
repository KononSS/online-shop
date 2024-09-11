package ru.obozhulkin.Onlineshop.conrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.obozhulkin.Onlineshop.exeption.ProductNotFoundException;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
public class GeneralController {

    private final ProductDetailsService productDetailsService;
    private final PersonDetailsService personDetailsService;

    public GeneralController(ProductDetailsService productDetailsService, PersonDetailsService personDetailsService) {
        this.productDetailsService = productDetailsService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        log.info("Accessed /user/hello endpoint");
        return "user/hello";
    }

    @GetMapping("/catalog")
    public String catalog(Model model) {
        log.info("Accessed /user/catalog endpoint");
        model.addAttribute("product", productDetailsService.findAll());
        return "user/showListProduct";
    }

    @GetMapping("/basket")
    public String basket(Model model) {
        log.info("Accessed /user/basket endpoint");
        List<Product> basket = personDetailsService.getProductByPersonId(personDetailsService.
                authenticatePerson().getPerson().getId());
        model.addAttribute("basket", basket);
        model.addAttribute("totalSum", basket.stream()
                .mapToDouble(Product::getPrice)
                .sum());
        return "user/basket";
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable("id") int idProduct) {
        log.info("Accessed /user/delete/{} endpoint", idProduct);
        personDetailsService.deleteProduct(personDetailsService.
                authenticatePerson().getPerson().getId(), idProduct);
        return "redirect:/user/basket";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("name") String name) {
        log.info("Accessed /user/search endpoint with name: {}", name);
        model.addAttribute("product", productDetailsService.searchByTitle(name));
        return "user/showListProduct";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.info("Accessed /user/{} endpoint", id);
        model.addAttribute("product", productDetailsService.findOne(id));
        return "user/showInfoProduct";
    }

    @PostMapping("/{id}")
    public String addInBasket(@PathVariable("id") int idProduct, RedirectAttributes redirectAttributes) {
        log.info("Accessed /user/{} endpoint for adding to basket", idProduct);
        try {
            Product product = productDetailsService.findOne(idProduct);
            if (product.getQuantity() == 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Товар закончился и не может быть добавлен в корзину.");
                return "redirect:/user/" + idProduct;
            }
            int personId = personDetailsService.authenticatePerson().getPerson().getId();
            personDetailsService.addBasket(personId, idProduct);
            redirectAttributes.addFlashAttribute("successMessage", "Товар успешно добавлен в корзину.");
        } catch (ProductNotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found: " + e.getMessage());
            return "redirect:/user/catalog";
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred: " + e.getMessage());
            return "redirect:/user/catalog";
        }
        return "redirect:/user/" + idProduct;
    }
}