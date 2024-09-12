package ru.obozhulkin.Onlineshop.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Person")
@Data
@NoArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть не менее 2-х символов и не более 100 символов")
    private String username;
    @Min(value = 1900, message = "Не меньше 1900")
    @Column(name = "year_of_birth")
    private int yearOfBirth;
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    @NotEmpty(message = "Введите номер телефона!")
    @Pattern(regexp = "^8\\d{10}$", message = "Номер телефона должен состоять из 11 цифр и быть в формате: 8**********")
    private String phone;
    @Column(name = "role")
    private String role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToMany
    @JoinTable(name = "person_product",
                joinColumns = @JoinColumn(name = "person_id"),
                inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> basket;

    public void addInBasket(Product product) {
        basket.add(product);
    }

    public void deleteOfBasket(Product product) {
        basket.remove(product);
    }
}