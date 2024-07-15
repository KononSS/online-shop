package ru.obozhulkin.Onlineshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.repositories.ProductRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailsService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductDetailsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> title(String title){
        return productRepository.findByTitle(title).stream().findAny();
    }

    @Transactional
    public void  registerProduct(Product product){
        productRepository.save(product);
    }

    public List<Product> findAll() {
            return productRepository.findAll();
    }

    public Product findOne(int id) {
        Optional<Product> foundProduct = productRepository.findById(id);
        return foundProduct.orElse(null);
    }
    public List<Product> searchByTitle(String name) {
        return productRepository.findByTitleStartingWith(name);
    }
}
