package ru.obozhulkin.Onlineshop.DTO;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ProductDTO {

    @NotEmpty(message = "Заполните название продукта")
    @Size(min = 2, max = 255, message = "Название должно быть не менее 2-х символов и не более 255 символов")
    private String title;
    @NotEmpty(message = "Заполните описание продукта")
    private String description;
    @NotEmpty(message = "Введите категорию товара")
    private String category;
    @Min(value = 1, message = "Стоимость не ниже 1")
    @Max(value = 1000000, message = "Стоимость не выше 1000000")
    private int price;
    private int quantity;
    private String image;
}
