package ru.obozhulkin.Onlineshop.REST;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.obozhulkin.Onlineshop.DTO.ProductDTO;
import ru.obozhulkin.Onlineshop.abstractClass.AbstractRestController;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
public class RESTcontroller extends AbstractRestController {

    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/img/snickers/";

    @Autowired
    public RESTcontroller(ProductDetailsService productDetailsService,
                    PersonDetailsService personDetailsService,
                    ProductValidator productValidator, ModelMapper modelMapper) {
        super(productDetailsService, personDetailsService, productValidator, modelMapper);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        logEndpointAccess("/api/hello");
        return ResponseEntity.ok("Hello from REST API Online-Shop");
    }

    @GetMapping("/allProducts")
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productDetailsService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        logEndpointAccess("/api/allProducts");
        return productDTOs;
    }


    @DeleteMapping("/delete/{id}")
    public HttpStatus delete(@PathVariable("id") int idProduct) {
        logEndpointAccess("/api/" + idProduct + " (delete)");
        productDetailsService.delete(idProduct);
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam("name") String name) {
        logEndpointAccess("/api/search?name=" + name);
        return productDetailsService.searchByTitle(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> show(@PathVariable("id") int id) {
        logEndpointAccess("/api/" + id + " (show)");
        return ResponseEntity.ok(productDetailsService.findOne(id));
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> performAddition(@ModelAttribute("product") @Valid ProductDTO productDTO,
                                             BindingResult bindingResult,
                                             @RequestParam("img") MultipartFile file) {

        logEndpointAccess("/api/addProduct");
        productValidator.validate(modelMapper.map(productDTO, Product.class), bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("Validation in API errors occurred while adding product");
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIRECTORY + file.getOriginalFilename());
                Files.write(path, bytes);
                productDTO.setImage("/img/snickers/" + file.getOriginalFilename());
                log.info("File uploaded successfully(API): {}", file.getOriginalFilename());
            }
            productDetailsService.registerProduct(modelMapper.map(productDTO, Product.class), personDetailsService.authenticatePerson().getPerson().getUsername());
            log.info("Product added successfully(API): {}", productDTO.getTitle());
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        } catch (IOException e) {
            log.error("File upload error(API): {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File upload error(API): " + e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred(API): {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
