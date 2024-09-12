package ru.obozhulkin.Onlineshop.conrollers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.obozhulkin.Onlineshop.DTO.ProductDTO;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductDetailsService productDetailsService;
    private final PersonDetailsService personDetailsService;
    private final ProductValidator productValidator;
    private final ModelMapper modelMapper;

    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/img/snickers/";

    @Autowired
    public AdminController(ModelMapper modelMapper, ProductDetailsService productService, ProductValidator personValidator, PersonDetailsService personDetailsService) {
        this.modelMapper = modelMapper;
        this.productDetailsService = productService;
        this.productValidator = personValidator;
        this.personDetailsService = personDetailsService;
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        log.info("Accessed /admin/adminPage endpoint");
        return "admin/adminPage";
    }

    @GetMapping("/addProduct")
    public String AdditionPage(@ModelAttribute("product") ProductDTO productDTO) {
        log.info("Accessed /admin/addProduct GET endpoint");
        return "admin/addition";
    }

    @PostMapping("/addProduct")
    public String performAddition(@ModelAttribute("product") @Valid ProductDTO productDTO,
                                  BindingResult bindingResult,
                                  @RequestParam("img") MultipartFile file) {
        log.info("Accessed /admin/addProduct POST endpoint");

        productValidator.validate(modelMapper.map(productDTO, Product.class), bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors occurred while adding product");
            return "admin/addition";
        }
        try {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIRECTORY + file.getOriginalFilename());
                Files.write(path, bytes);
                productDTO.setImage("/img/snickers/" + file.getOriginalFilename());
                log.info("File uploaded successfully: {}", file.getOriginalFilename());
            }
            productDetailsService.registerProduct(modelMapper.map(productDTO, Product.class), personDetailsService.authenticatePerson().getPerson().getUsername());
            log.info("Product added successfully: {}", productDTO.getTitle());
        } catch (IOException e) {
            log.error("File upload error: {}", e.getMessage());
            bindingResult.reject("file.upload.error", "File upload error: " + e.getMessage());
            return "admin/addition";
        }

        return "admin/adminPage";
    }
}

