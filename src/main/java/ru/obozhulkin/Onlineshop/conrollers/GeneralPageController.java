package ru.obozhulkin.Onlineshop.conrollers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.security.PersonDetails;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;


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
        model.addAttribute("product", productDetailsService.findAll());
        return "user/basket";
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
    @PatchMapping("/{id}")
    public String addInBasket(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        personDetailsService.addBasket(id, person);
        return "user/showInfoProduct";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());

        return "user/hello";
    }
}

