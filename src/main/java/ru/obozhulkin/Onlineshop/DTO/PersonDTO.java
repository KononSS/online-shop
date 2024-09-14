package ru.obozhulkin.Onlineshop.DTO;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO для представления пользователя.
 */
@Data
public class PersonDTO {

    /** Имя пользователя. */
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть не менее 2-х символов и не более 100 символов")
    private String username;

    /** Год рождения пользователя. */
    @Min(value = 1900, message = "Не меньнее 1900")
    private int yearOfBirth;

    /** Пароль пользователя. */
    @NotEmpty(message = "пароль не должен быть пустым")
    private String password;

    /** Номер телефона пользователя. */
    @NotEmpty(message = "Введите номер телефона!")
    @Pattern(regexp = "^8\\d{10}$", message = "Номер телефона должен состоять из 11 цифр и быть в формате: 8**********")
    private String phone;

    /** Роль пользователя. */
    private String role;
}
