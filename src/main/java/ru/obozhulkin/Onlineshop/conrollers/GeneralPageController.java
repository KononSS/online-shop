package ru.obozhulkin.Onlineshop.conrollers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.obozhulkin.Onlineshop.security.PersonDetails;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;


@Controller
@RequestMapping("/user")
public class GeneralPageController {

    private final ProductDetailsService productDetailsService;
//
    public GeneralPageController(ProductDetailsService productDetailsService) {
        this.productDetailsService = productDetailsService;
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
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productDetailsService.findOne(id));

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

