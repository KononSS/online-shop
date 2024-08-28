package ru.obozhulkin.Onlineshop.conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductDetailsService productDetailsService;
    private final ProductValidator productValidator;

    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/img/snickers/";

@Autowired
    public AdminController(ProductDetailsService additionService, ProductValidator personValidator) {
        this.productDetailsService = additionService;
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
public String performAddition(@ModelAttribute("product") @Valid Product product,
                              BindingResult bindingResult,
                              @RequestParam("image") MultipartFile file) {
    productValidator.validate(product, bindingResult);
    if (bindingResult.hasErrors()) {
        return "admin/addition";
    }

    if (!file.isEmpty()) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIRECTORY + file.getOriginalFilename());
            Files.write(path, bytes);
            product.setImage_url("/img/snickers/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    productDetailsService.registerProduct(product);
    return "admin/adminPage";
}
}

