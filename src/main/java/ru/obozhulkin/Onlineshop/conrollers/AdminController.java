package ru.obozhulkin.Onlineshop.conrollers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.obozhulkin.Onlineshop.DTO.ProductDTO;
import ru.obozhulkin.Onlineshop.models.Category;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;
import ru.obozhulkin.Onlineshop.services.ProductDetailsService;
import ru.obozhulkin.Onlineshop.util.ProductValidator;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Контроллер для административной панели.
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    /** Сервис для работы с продуктами. */
    private final ProductDetailsService productDetailsService;
    /** Сервис для работы с пользователями. */
    private final PersonDetailsService personDetailsService;
    /** Валидатор для продуктов. */
    private final ProductValidator productValidator;
    /** Маппер для преобразования объектов. */
    private final ModelMapper modelMapper;

    /** Директория для загрузки файлов. */
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/img/snickers/";

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param modelMapper Маппер для преобразования объектов.
     * @param productService Сервис для работы с продуктами.
     * @param productValidator Валидатор для продуктов.
     * @param personDetailsService Сервис для работы с пользователями.
     */
    @Autowired
    public AdminController(ModelMapper modelMapper, ProductDetailsService productService, ProductValidator productValidator, PersonDetailsService personDetailsService) {
        this.modelMapper = modelMapper;
        this.productDetailsService = productService;
        this.productValidator = productValidator;
        this.personDetailsService = personDetailsService;
    }

    /**
     * Обрабатывает GET-запрос на страницу администратора.
     *
     * @return Имя представления для страницы администратора.
     */
    @GetMapping("/adminPage")
    public String adminPage(Model model) {
        log.info("Accessed /admin/adminPage endpoint");
        model.addAttribute("category", productDetailsService.findAllCategory());
        model.addAttribute("newCategory", new Category()); // Добавьте эту строку
        return "admin/adminPage";
    }

    /**
     * Обрабатывает POST-запрос на удаление категории.
     *
     * @param idCategoty Идентификатор категории для удаления.
     * @return Перенаправление на страницу администратора.
     */
    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") long idCategoty) {
        log.info("Deleting category with id: {}", idCategoty);
        productDetailsService.deleteCategory(idCategoty);
        return "redirect:/admin/adminPage";
    }

    /**
     * Обрабатывает POST-запрос на добавление новой категории.
     *
     * @param category Объект категории для добавления.
     * @param bindingResult Результат валидации.
     * @param model Модель для передачи данных в представление.
     * @return Перенаправление на страницу администратора или возврат на ту же страницу с ошибками валидации.
     */
    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, Model model) {
        log.info("Adding new category: {}", category.getName());
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors occurred while adding category: {}", bindingResult.getAllErrors());
            model.addAttribute("categories", productDetailsService.findAllCategories());
            return "admin/adminPage";
        }
        try {
            productDetailsService.saveCategory(category);
            log.info("Category added successfully: {}", category.getName());
        } catch (IllegalArgumentException e) {
            log.error("Category with name '{}' already exists", category.getName());
            bindingResult.rejectValue("name", "category.name.duplicate", e.getMessage());
            model.addAttribute("categories", productDetailsService.findAllCategories());
            return "admin/adminPage";
        }
        return "redirect:/admin/adminPage";
    }

    /**
     * Обрабатывает GET-запрос на отображение списка продуктов по категории.
     *
     * @param id Идентификатор категории.
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для отображения списка продуктов по категории.
     */
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        log.info("Accessed /user/{} endpoint", id);
        model.addAttribute("product", productDetailsService.findSnickersByCategory(id));
        return "admin/showListProductByCategory";
    }

    /**
     * Обрабатывает POST-запрос на удаление продукта.
     *
     * @param idProduct Идентификатор продукта для удаления.
     * @return Перенаправление на страницу администратора.
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int idProduct) {
        log.info("Deleting product with id: {}", idProduct);
        productDetailsService.delete(idProduct);
        return "redirect:/admin/adminPage";
    }

    /**
     * Обрабатывает GET-запрос на страницу добавления продукта.
     *
     * @param productDTO Объект DTO для продукта.
     * @return Имя представления для страницы добавления продукта.
     */
    @GetMapping("/addProduct")
    public String AdditionPage(@ModelAttribute("product") ProductDTO productDTO, Model model) {
        List<Category> categories = productDetailsService.findAllCategories();
        model.addAttribute("categories", categories);
        log.info("Accessed /admin/addProduct GET endpoint");
        return "admin/addition";
    }

    /**
     * Обрабатывает POST-запрос на добавление продукта.
     *
     * @param productDTO Объект DTO для продукта.
     * @param bindingResult Результат валидации.
     * @param file Файл изображения продукта.
     * @return Имя представления для страницы администратора или страницы добавления продукта в случае ошибки.
     */
    @PostMapping("/addProduct")
    public String performAddition(@ModelAttribute("product") @Valid ProductDTO productDTO,
                                  BindingResult bindingResult,
                                  @RequestParam("img") MultipartFile file,
                                  Model model) {
        log.info("Accessed /admin/addProduct POST endpoint");
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors occurred while adding product");
            List<Category> categories = productDetailsService.findAllCategories();
            model.addAttribute("categories", categories);
            return "admin/addition";
        }
        if (file == null || file.isEmpty()) {
            log.warn("File upload error: file is null or empty");
            bindingResult.rejectValue("img", "file.upload.required", "Загрузите картинку");
            List<Category> categories = productDetailsService.findAllCategories();
            model.addAttribute("categories", categories);
            return "admin/addition";
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIRECTORY + file.getOriginalFilename());
            Files.write(path, bytes);
            productDTO.setImage("/img/snickers/" + file.getOriginalFilename());
            log.info("File uploaded successfully: {}", file.getOriginalFilename());
            Product product = modelMapper.map(productDTO, Product.class);
            Category category = productDetailsService.searchCategory(productDTO.getCategoryName());
            product.setCategory(category);
            productDetailsService.registerProduct(product, personDetailsService.authenticatePerson().getPerson().getUsername());
            log.info("Product added successfully: {}", productDTO.getTitle());
        } catch (IOException e) {
            log.error("File upload error: {}", e.getMessage());
            bindingResult.reject("file.upload.error", "File upload error: " + e.getMessage());
            List<Category> categories = productDetailsService.findAllCategories();
            model.addAttribute("categories", categories);
            return "admin/addition";
        }

        return "redirect:/admin/adminPage";
    }
}

