package ru.obozhulkin.Onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.obozhulkin.Onlineshop.models.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional <Product> findByTitle(String title);
}
