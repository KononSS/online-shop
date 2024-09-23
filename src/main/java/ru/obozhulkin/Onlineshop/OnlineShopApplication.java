package ru.obozhulkin.Onlineshop;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.obozhulkin.Onlineshop.DTO.ProductDTO;
import ru.obozhulkin.Onlineshop.models.Product;

/**
 * Основной класс приложения Online Shop.
 * Этот класс запускает Spring Boot приложение.
 */
@SpringBootApplication
public class OnlineShopApplication {

	/**
	 * Основной метод, который запускает приложение.
	 *
	 * @param args Аргументы командной строки.
	 */
	public static void main(String[] args) {
		SpringApplication.run(OnlineShopApplication.class, args);
	}

	/**
	 * Создает и настраивает бин ModelMapper.
	 *
	 * @return Настроенный экземпляр ModelMapper.
	 */
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<Product, ProductDTO>() {
			@Override
			protected void configure() {
				map().setCategoryName(source.getCategory().getName());
			}
		});
		return modelMapper;
	}
}
