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

/**
 * Контроллер для обработки REST API запросов.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class RESTcontroller extends AbstractRestController {

    /** Директория для загрузки файлов. */
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/img/snickers/";

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param productDetailsService Сервис для работы с продуктами.
     * @param personDetailsService Сервис для работы с пользователями.
     * @param productValidator Валидатор для продуктов.
     * @param modelMapper Маппер для преобразования объектов.
     */
    @Autowired
    public RESTcontroller(ProductDetailsService productDetailsService,
                          PersonDetailsService personDetailsService,
                          ProductValidator productValidator, ModelMapper modelMapper) {
        super(productDetailsService, personDetailsService, productValidator, modelMapper);
    }

    /**
     * Обрабатывает GET-запрос на приветствие.
     *
     * @return Ответ с сообщением приветствия.
     */
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        logEndpointAccess("/api/hello");
        return ResponseEntity.ok("Hello from REST API Online-Shop");
    }

    /**
     * Обрабатывает GET-запрос на получение всех продуктов.
     *
     * @return Список DTO продуктов.
     */
    @GetMapping("/allProducts")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productDetailsService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        logEndpointAccess("/api/allProducts");
        return ResponseEntity.ok(productDTOs);
    }

    /**
     * Обрабатывает DELETE-запрос на удаление продукта.
     *
     * @param idProduct Идентификатор продукта для удаления.
     * @return Статус ответа.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int idProduct) {
        logEndpointAccess("/api/" + idProduct + " (delete)");
        productDetailsService.delete(idProduct);
        return ResponseEntity.noContent().build();
    }

    /**
     * Обрабатывает GET-запрос на поиск продуктов.
     *
     * @param name Название продукта для поиска.
     * @return Список найденных продуктов.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> search(@RequestParam("name") String name) {
        logEndpointAccess("/api/search?name=" + name);
        List<Product> products = productDetailsService.searchByTitle(name);
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    /**
     * Обрабатывает GET-запрос на получение информации о продукте.
     *
     * @param id Идентификатор продукта.
     * @return Ответ с информацией о продукте.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> show(@PathVariable("id") int id) {
        logEndpointAccess("/api/" + id + " (show)");
        Product product = productDetailsService.findOne(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Обрабатывает POST-запрос на добавление продукта.
     *
     * @param productDTO Объект DTO для продукта.
     * @param bindingResult Результат валидации.
     * @param file Файл изображения продукта.
     * @return Ответ с результатом добавления продукта.
     */
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