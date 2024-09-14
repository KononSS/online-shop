package ru.obozhulkin.Onlineshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.obozhulkin.Onlineshop.exeption.PersonNotFoundException;
import ru.obozhulkin.Onlineshop.exeption.ProductNotFoundException;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.repositories.PeopleRepository;
import ru.obozhulkin.Onlineshop.repositories.ProductRepository;
import ru.obozhulkin.Onlineshop.security.PersonDetails;
import java.util.*;


/**
 * Сервис для работы с пользователями.
 */
@Slf4j
@Service
public class PersonDetailsService implements UserDetailsService {

    /** Репозиторий для работы с пользователями. */
    private final PeopleRepository peopleRepository;

    /** Репозиторий для работы с продуктами. */
    private final ProductRepository productRepository;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param peopleRepository Репозиторий для работы с пользователями.
     * @param productRepository Репозиторий для работы с продуктами.
     */
    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository, ProductRepository productRepository) {
        this.peopleRepository = peopleRepository;
        this.productRepository = productRepository;
    }

    /**
     * Аутентифицирует текущего пользователя.
     *
     * @return Детали аутентифицированного пользователя.
     */
    public PersonDetails authenticatePerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authenticating person: {}", authentication.getName());
        return (PersonDetails) authentication.getPrincipal();
    }

    /**
     * Находит продукт по идентификатору.
     *
     * @param id Идентификатор продукта.
     * @return Продукт.
     * @throws ProductNotFoundException Если продукт не найден.
     */
    public Product product(int id) {
        log.debug("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException("Product with id " + id + " not found");
                });
    }

    /**
     * Загружает пользователя по имени.
     *
     * @param s Имя пользователя.
     * @return Детали пользователя.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", s);
        Optional<Person> person = peopleRepository.findByUsername(s);

        if (person.isEmpty()) {
            log.error("User with username {} not found", s);
            throw new UsernameNotFoundException("Пользователь не найден!");
        }

        log.debug("User found: {}", person.get());
        return new PersonDetails(person.get());
    }

    /**
     * Находит пользователя по номеру телефона.
     *
     * @param phone Номер телефона пользователя.
     * @return Optional с найденным пользователем.
     */
    public Optional<Person> showPhone(String phone) {
        log.debug("Finding person by phone: {}", phone);
        return peopleRepository.findByPhone(phone).stream().findAny();
    }

    /**
     * Добавляет продукт в корзину пользователя.
     *
     * @param personId Идентификатор пользователя.
     * @param productId Идентификатор продукта.
     * @throws PersonNotFoundException Если пользователь не найден.
     * @throws ProductNotFoundException Если продукт не найден.
     * @throws IllegalStateException Если продукт закончился.
     */
    @Transactional
    public void addBasket(int personId, int productId) {
        log.debug("Adding product {} to basket for person {}", productId, personId);
        Person person = peopleRepository.findById(personId)
                .orElseThrow(() -> {
                    log.error("Person with id {} not found for addition in basket", personId);
                    return new PersonNotFoundException("Person with id " + personId + " not found");
                });
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found for addition in basket", productId);
                    return new ProductNotFoundException("Product with id " + productId + " not found");
                });

        int counter = product.getQuantity();
        if (counter <= 0) {
            log.error("Product {} is out of stock", productId);
            throw new IllegalStateException("Product is out of stock");
        }
        counter = counter - 1;
        product.setQuantity(counter);
        person.addInBasket(product);
        product.addBuyer(person);
        log.debug("Product {} added to basket for person {}", productId, personId);
    }

    /**
     * Удаляет продукт из корзины пользователя.
     *
     * @param personId Идентификатор пользователя.
     * @param productId Идентификатор продукта.
     * @throws PersonNotFoundException Если пользователь не найден.
     * @throws ProductNotFoundException Если продукт не найден.
     */
    @Transactional
    public void deleteProduct(int personId, int productId) {
        log.debug("Deleting product {} from basket for person {}", productId, personId);
        Person person = peopleRepository.findById(personId)
                .orElseThrow(() -> {
                    log.error("Person with id {} not found for delete of basket", personId);
                    return new PersonNotFoundException("Person with id " + personId + " not found");
                });
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found for delete of basket", productId);
                    return new ProductNotFoundException("Product with id " + productId + " not found");
                });

        product.setQuantity(product.getQuantity() + 1);
        person.deleteOfBasket(product);
        log.debug("Product {} deleted from basket for person {}", productId, personId);
    }

    /**
     * Получает продукты в корзине пользователя.
     *
     * @param id Идентификатор пользователя.
     * @return Список продуктов в корзине.
     * @throws PersonNotFoundException Если пользователь не найден.
     */
    public List<Product> getProductByPersonId(int id) {
        log.debug("Getting products for person with id: {}", id);
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            log.debug("Found {} products for person with id: {}", person.get().getBasket().size(), id);
            return person.get().getBasket();
        } else {
            log.error("Person with id {} not found", id);
            throw new PersonNotFoundException("Person with id " + id + " not found");
        }
    }
}

