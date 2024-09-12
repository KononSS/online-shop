package ru.obozhulkin.Onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.obozhulkin.Onlineshop.models.Product;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional <Product> findByTitle(String title);
    @Query("select p from Product p where lower(p.title) like lower(concat(:name, '%'))")
    List<Product> findByTitleStartingWith(String name);

}
