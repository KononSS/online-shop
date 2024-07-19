package ru.obozhulkin.Onlineshop.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть не менее 2-х символов и не более 100 символов")
    private String username;
    @Min(value = 1900, message = "Не меньнее 1900")
    @Column(name = "year_of_birth")
    private int yearOfBirth;
    @NotEmpty(message = "пароль не должен быть пустым")
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    @NotEmpty(message = "Введите номер телефона!")
    @Pattern(regexp = "^8\\d{10}$", message = "Номер телефона должен состоять из 11 цифр и быть в формате: 8**********")
    private String phone;
    @Column(name = "role")
    private String role;
    @OneToMany(mappedBy = "buyers")
    private List<Product> basket;




//    public Person(String username, int yearOfBirth) {
//        this.username = username;
//        this.yearOfBirth = yearOfBirth;
//    }
    public Person(){}

    public List<Product> getBasket() {
        return basket;
    }

    public void setBasket(List<Product> basket) {
        this.basket = basket;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", password='" + password + '\'' +
                ", phone=" + phone +
                ", role='" + role + '\'' +
                '}';
    }
}
