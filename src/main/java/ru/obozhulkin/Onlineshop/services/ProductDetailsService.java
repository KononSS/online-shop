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

@Slf4j
@Service
public class ProductDetailsService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductDetailsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> title(String title) {
        log.debug("Finding product by title: {}", title);
        return productRepository.findByTitle(title).stream().findAny();
    }

    @Transactional
    public void registerProduct(Product product, String adminName) {
        log.debug("Registering product: {}", product);
        enrichProduct(product, adminName);
        productRepository.save(product);
        log.debug("Product registered: {}", product);
    }

    public List<Product> findAll() {
        log.debug("Finding all products");
        return productRepository.findAll();
    }

    public Product findOne(int id) {
        log.debug("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException("Product with id " + id + " not found");
                });
    }

    public void delete(int id) {
        log.debug("Deleting product: {}", id);
        productRepository.deleteById(id);
    }

    public List<Product> searchByTitle(String name) {
        log.debug("Searching products by title starting with: {}", name);
        return productRepository.findByTitleStartingWith(name);
    }

    private void enrichProduct(Product product, String adminName) {
        log.debug("Enriching product: {}", product);
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedWho(adminName);
        log.debug("Product enriched: {}", product);
    }
}