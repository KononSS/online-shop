package ru.obozhulkin.Onlineshop.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.*;
import java.math.BigDecimal;

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
    private String categoryName;

    /** Цена продукта. */
    @NotNull(message = "Введите стоимость")
    @Min(value = 1, message = "Стоимость не ниже 1")
    @Max(value = 1000000, message = "Стоимость не выше 1000000")
    private BigDecimal price;

    /** Количество продукта. */
    private int quantity;

    /** Относительный адрес изображения продукта. */
    private String image;

    /** Файл изображения продукта. */
    private MultipartFile img;

}
