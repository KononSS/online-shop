package ru.obozhulkin.Onlineshop.DTO;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PersonDTO {
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть не менее 2-х символов и не более 100 символов")
    private String username;
    @Min(value = 1900, message = "Не меньнее 1900")
    private int yearOfBirth;
    @NotEmpty(message = "пароль не должен быть пустым")
    private String password;
    @NotEmpty(message = "Введите номер телефона!")
    @Pattern(regexp = "^8\\d{10}$", message = "Номер телефона должен состоять из 11 цифр и быть в формате: 8**********")
    private String phone;
    private String role;
}
