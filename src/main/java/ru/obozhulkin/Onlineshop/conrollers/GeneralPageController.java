package ru.obozhulkin.Onlineshop.conrollers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.security.PersonDetails;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;

import java.util.List;


@Controller
@RequestMapping("/user")
public class GeneralPageController {

    private final ProductDetailsService productDetailsService;
    private final PersonDetailsService personDetailsService;

    //
    public GeneralPageController(ProductDetailsService productDetailsService, PersonDetailsService personDetailsService) {
        this.productDetailsService = productDetailsService;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "user/hello";
    }

    @GetMapping("/catalog")
    public String catalog(Model model) {
        model.addAttribute("product", productDetailsService.findAll());
        return "user/showListProduct";
    }

    @GetMapping("/basket")
    public String basket(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Product> basket = personDetailsService.getProductByPersonId(personDetails.getPerson().getId());
        double totalSum=0;
        for (Product product : basket) {
            totalSum+=product.getPrice();
        }
     //   model.addAttribute("person", personDetailsService.findOne(personDetails.getPerson().getId()));
        model.addAttribute("basket", basket);
        model.addAttribute("totalSum", totalSum);
        return "user/basket";
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        personDetailsService.deleteProduct(personDetails.getPerson().getId(), id);
        return "redirect:/user/basket";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("name") String name) {
        model.addAttribute("product", productDetailsService.searchByTitle(name));
        return "user/showListProduct";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productDetailsService.findOne(id));
        return "user/showInfoProduct";
    }
    @PostMapping("/{id}")
    public String addInBasket(@PathVariable("id") int idProduct, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Product product = productDetailsService.findOne(idProduct);
        if (product.getQuantity() == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Товар закончился и не может быть добавлен в корзину.");
            return "redirect:/user/"+idProduct;
        }
        personDetailsService.addBasket(personDetails.getPerson().getId(),idProduct);
        return "redirect:/user/"+idProduct;
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());

        return "user/hello";
    }
}

