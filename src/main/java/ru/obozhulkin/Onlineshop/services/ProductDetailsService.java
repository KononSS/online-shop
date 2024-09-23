package ru.obozhulkin.Onlineshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.obozhulkin.Onlineshop.exeption.ProductNotFoundException;
import ru.obozhulkin.Onlineshop.models.Category;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.repositories.CategoryRepository;
import ru.obozhulkin.Onlineshop.repositories.ProductRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с продуктами и категориями.
 */
@Slf4j
@Service
public class ProductDetailsService {

    /** Репозиторий для работы с продуктами. */
    private final ProductRepository productRepository;

    /** Репозиторий для работы с категориями. */
    private final CategoryRepository categoryRepository;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param productRepository Репозиторий для работы с продуктами.
     * @param categoryRepository Репозиторий для работы с категориями.
     */
    @Autowired
    public ProductDetailsService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Находит продукт по названию.
     *
     * @param title Название продукта.
     * @return Optional с найденным продуктом.
     */
    public Optional<Product> title(String title) {
        log.debug("Finding product by title: {}", title);
        return productRepository.findByTitle(title).stream().findAny();
    }

    /**
     * Находит категорию по названию.
     *
     * @param title Название категории.
     * @return Optional с найденной категорией.
     */
    public Optional<Category> categoryName(String title) {
        log.debug("Finding category by name: {}", title);
        return categoryRepository.findByName(title).stream().findAny();
    }

    /**
     * Регистрирует новый продукт.
     *
     * @param product Продукт для регистрации.
     * @param adminName Имя администратора, создавшего продукт.
     */
    @Transactional
    public void registerProduct(Product product, String adminName) {
        log.debug("Registering product: {}", product);
        try {
            enrichProduct(product, adminName);
            Category category = product.getCategory();

            // Проверяем, существует ли уже категория с таким именем
            Optional<Category> existingCategoryOptional = categoryRepository.findByName(category.getName());
            if (existingCategoryOptional.isPresent()) {
                log.debug("Category already exists with name: {}", category.getName());
                product.setCategory(existingCategoryOptional.get());
            } else {
                log.debug("Category does not exist, saving new category: {}", category);
                categoryRepository.save(category);
            }

            productRepository.save(product);
            log.debug("Product registered: {}", product);

        } catch (Exception e) {
            log.error("Failed to register product: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register product", e);
        }
    }

    /**
     * Находит все категории.
     *
     * @return Список всех категорий.
     */
    public List<Category> findAllCategories() {
        log.debug("Finding all categories");
        return categoryRepository.findAll();
    }

    /**
     * Ищет категорию по названию.
     *
     * @param categoryName Название категории.
     * @return Найденная категория.
     * @throws IllegalArgumentException Если категория не найдена.
     */
    public Category searchCategory(String categoryName) {
        log.debug("Searching category by name: {}", categoryName);
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> {
                    log.error("Category with name {} not found", categoryName);
                    return new IllegalArgumentException("Invalid category name: " + categoryName);
                });
    }

    /**
     * Находит все продукты.
     *
     * @return Список всех продуктов.
     */
    public List<Product> findAll() {
        log.debug("Finding all products");
        return productRepository.findAll();
    }

    /**
     * Находит все категории.
     *
     * @return Список всех категорий.
     */
    public List<Category> findAllCategory() {
        log.debug("Finding all categories");
        return categoryRepository.findAll();
    }

    /**
     * Находит продукты по идентификатору категории.
     *
     * @param id Идентификатор категории.
     * @return Список продуктов.
     */
    public List<Product> findSnickersByCategory(long id) {
        log.debug("Finding products by category id: {}", id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            return productRepository.findByCategory(category.get());
        } else {
            log.warn("Category with id {} not found", id);
            return Collections.emptyList();
        }
    }

    /**
     * Находит продукт по идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Продукт.
     * @throws ProductNotFoundException Если продукт не найден.
     */
    public Product findOne(int id) {
        log.debug("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException("Product with id " + id + " not found");
                });
    }

    /**
     * Удаляет продукт по идентификатору.
     *
     * @param id Идентификатор продукта.
     */
    public void delete(int id) {
        log.debug("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }

    /**
     * Сохраняет новую категорию.
     *
     * @param category Категория для сохранения.
     * @throws IllegalArgumentException Если категория с таким именем уже существует.
     */
    public void saveCategory(Category category) {
        log.debug("Saving new category: {}", category.getName());
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            log.error("Category with name '{}' already exists", category.getName());
            throw new IllegalArgumentException("Категория с таким именем уже существует");
        }
        categoryRepository.save(category);
        log.debug("Category saved successfully: {}", category.getName());
    }

    /**
     * Удаляет категорию по идентификатору.
     *
     * @param id Идентификатор категории.
     */
    public void deleteCategory(long id) {
        log.debug("Deleting category with id: {}", id);
        categoryRepository.deleteById(id);
    }

    /**
     * Ищет продукты по названию, начинающемуся с указанной строки.
     *
     * @param name Начальная строка названия продукта.
     * @return Список найденных продуктов.
     */
    public List<Product> searchByTitle(String name) {
        log.debug("Searching products by title starting with: {}", name);
        return productRepository.findByTitleStartingWith(name);
    }

    /**
     * Дополняет продукт дополнительными данными.
     *
     * @param product Продукт для дополнения.
     * @param adminName Имя администратора, создавшего продукт.
     */
    private void enrichProduct(Product product, String adminName) {
        log.debug("Enriching product: {}", product);
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedWho(adminName);
        log.debug("Product enriched: {}", product);
    }
}