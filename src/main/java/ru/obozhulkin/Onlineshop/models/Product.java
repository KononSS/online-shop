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

@Entity
@Table(name = "Product")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;

    @Column(name = "title")
    @NotEmpty(message = "Заполните название продукта")
    @Size(min = 2, max = 255, message = "Название должно быть не менее 2-х символов и не более 255 символов")
    private String title;

    @NotEmpty(message = "Заполните описание продукта")
    @Column(name = "description")
    private String description;

    @NotEmpty(message = "Введите категорию товара")
    @Column(name = "category")
    private String category;

    @Column(name = "price")
    @Min(value = 1, message = "Стоимость не ниже 1")
    @Max(value = 1000000, message = "Стоимость не выше 1000000")
    private int price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "image")
    private String image;

    @ManyToMany(mappedBy = "basket")
    private List<Person> buyers;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_who")
    @NotEmpty
    private String createdWho;

    public Product(Optional<Person> byId) {}

    public void addBuyer(Person buyer) {
        buyers.add(buyer);
    }

    public void deleteBuyer(Person buyer) {
        buyers.remove(buyer);
    }
}