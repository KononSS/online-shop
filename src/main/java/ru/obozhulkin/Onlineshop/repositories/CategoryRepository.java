package ru.obozhulkin.Onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.obozhulkin.Onlineshop.models.Category;

import java.util.Optional;

/**
 * Репозиторий для работы с категориями.
 * Расширяет JpaRepository для выполнения CRUD операций над сущностью Category.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Находит категорию по имени.
     *
     * @param categoryName Имя категории.
     * @return Optional объект категории, если найдена, иначе пустой Optional.
     */
    Optional<Category> findByName(String categoryName);
}
