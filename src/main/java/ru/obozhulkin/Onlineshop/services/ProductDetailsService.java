package ru.obozhulkin.Onlineshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.obozhulkin.Onlineshop.exeption.ProductNotFoundException;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.repositories.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с продуктами.
 */
@Slf4j
@Service
public class ProductDetailsService {
    /** Репозиторий для работы с продуктами. */
    private final ProductRepository productRepository;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param productRepository Репозиторий для работы с продуктами.
     */
    @Autowired
    public ProductDetailsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
     * Регистрирует новый продукт.
     *
     * @param product Продукт для регистрации.
     * @param adminName Имя администратора, создавшего продукт.
     */
    @Transactional
    public void registerProduct(Product product, String adminName) {
        log.debug("Registering product: {}", product);
        enrichProduct(product, adminName);
        productRepository.save(product);
        log.debug("Product registered: {}", product);
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
        log.debug("Deleting product: {}", id);
        productRepository.deleteById(id);
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