package ru.obozhulkin.Onlineshop.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "Product")
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
    @Column(name = "category_id")
    private String category_id;
    @Column(name = "price")
    @Min(value = 1, message = "Стоимость не ниже 1")
    @Max(value = 1000000, message = "Стоимость не выше 1000000")
    private int price;
    @Column(name = "quantity")
    @NotEmpty(message = "Введите колличество")
    private String quantity;
    @Column(name = "image_url")
    @NotEmpty(message = "Введите url картинки товара")
    private String image_url;
    @ManyToMany(mappedBy = "basket")
    private List<Person> buyers;


    public Product(Optional<Person> byId) {}

    public Product() {}

    public List<Person> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<Person> buyers) {
        this.buyers = buyers;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category_id='" + category_id + '\'' +
                ", price=" + price +
                ", quantity='" + quantity + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
