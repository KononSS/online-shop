package ru.obozhulkin.Onlineshop.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сущность, представляющая продукт.
 */
@Entity
@Table(name = "Product")
@Data
@NoArgsConstructor
public class Product {

    /** Идентификатор продукта. */
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;

    /** Название продукта. */
    @Column(name = "title")
    @NotEmpty(message = "Заполните название продукта")
    @Size(min = 2, max = 255, message = "Название должно быть не менее 2-х символов и не более 255 символов")
    private String title;

    /** Описание продукта. */
    @NotEmpty(message = "Заполните описание продукта")
    @Column(name = "description")
    private String description;

    /** Категория продукта. */
    @NotEmpty(message = "Введите категорию товара")
    @Column(name = "category")
    private String category;

    /** Цена продукта. */
    @Column(name = "price")
    @Min(value = 1, message = "Стоимость не ниже 1")
    @Max(value = 1000000, message = "Стоимость не выше 1000000")
    private int price;

    /** Количество продукта. */
    @Column(name = "quantity")
    private int quantity;

    /** Относительный адрес изображение продукта. */
    @Column(name = "image")
    private String image;

    /** Список покупателей продукта. */
    @ManyToMany(mappedBy = "basket")
    private List<Person> buyers;

    /** Дата создания продукта. */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Пользователь, создавший продукт. */
    @Column(name = "created_who")
    @NotEmpty
    private String createdWho;

    /**
     * Конструктор для создания продукта на основе пользователя.
     *
     * @param byId Пользователь, создавший продукт.
     */
    public Product(Optional<Person> byId) {}

    /**
     * Добавляет покупателя продукта.
     *
     * @param buyer Покупатель продукта.
     */
    public void addBuyer(Person buyer) {
        buyers.add(buyer);
    }
}