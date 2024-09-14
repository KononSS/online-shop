package ru.obozhulkin.Onlineshop.DTO;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO для представления продукта.
 */
@Data
public class ProductDTO {

    /** Название продукта. */
    @NotEmpty(message = "Заполните название продукта")
    @Size(min = 2, max = 255, message = "Название должно быть не менее 2-х символов и не более 255 символов")
    private String title;

    /** Описание продукта. */
    @NotEmpty(message = "Заполните описание продукта")
    private String description;

    /** Категория продукта. */
    @NotEmpty(message = "Введите категорию товара")
    private String category;

    /** Цена продукта. */
    @Min(value = 1, message = "Стоимость не ниже 1")
    @Max(value = 1000000, message = "Стоимость не выше 1000000")
    private int price;

    /** Количество продукта. */
    private int quantity;

    /** Относительный адрес изображения продукта. */
    private String image;
}
