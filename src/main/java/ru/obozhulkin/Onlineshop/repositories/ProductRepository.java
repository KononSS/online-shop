package ru.obozhulkin.Onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.obozhulkin.Onlineshop.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional <Product> findByTitle(String title);
    List<Product> findByTitleStartingWith(String name);
    @Query("update Product set buyers = :personId where product_id = :productId")
    void addPersonIdInTableProduct(@Param("productId") int productId, @Param("personId") int personId);
}
