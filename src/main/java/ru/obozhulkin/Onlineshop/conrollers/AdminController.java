package ru.obozhulkin.Onlineshop.conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductDetailsService productDetailsService;
    private final ProductValidator productValidator;

@Autowired
    public AdminController(ProductDetailsService additionSevice, ProductValidator personValidator) {
        this.productDetailsService = additionSevice;
        this.productValidator = personValidator;
    }

    @GetMapping("/adminPage")
    public String adminPage(){
        return "admin/adminPage";
    }

    @GetMapping("/addProduct")
    public String AdditionPage(@ModelAttribute("product") Product product){
        return "admin/addition";
    }

    @PostMapping("/addProduct")
    public String performAddition(@ModelAttribute("product")@Valid Product product, BindingResult bindingResult){
        productValidator.validate(product,bindingResult);
        if (bindingResult.hasErrors())
            return "admin/addition";
        productDetailsService.registerProduct(product);
        return "admin/adminPage";
    }
}

