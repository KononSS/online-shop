package ru.obozhulkin.Onlineshop.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Сущность, представляющая категорию товаров в интернет-магазине.
 */
@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
public class Category {

    /**
     * Уникальный идентификатор категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Название категории. Не может быть пустым.
     */
    @NotEmpty(message = "Название категории не может быть пустым")
    private String name;

    /**
     * Список продуктов, принадлежащих данной категории.
     * Связь один-ко-многим с сущностью Product.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

}
