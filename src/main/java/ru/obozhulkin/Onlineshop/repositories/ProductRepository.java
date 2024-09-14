package ru.obozhulkin.Onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.obozhulkin.Onlineshop.models.Product;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Product.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Находит продукт по названию.
     *
     * @param title Название продукта.
     * @return Optional с найденным продуктом.
     */
    Optional<Product> findByTitle(String title);

    /**
     * Находит продукты, название которых начинается с указанной строки.
     *
     * @param name Начальная строка названия продукта.
     * @return Список найденных продуктов.
     */
    @Query("select p from Product p where lower(p.title) like lower(concat(:name, '%'))")
    List<Product> findByTitleStartingWith(String name);
}
