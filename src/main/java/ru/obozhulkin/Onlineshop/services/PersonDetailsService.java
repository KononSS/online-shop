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


@Slf4j
@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;
    private final ProductRepository productRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository, ProductRepository productRepository) {
        this.peopleRepository = peopleRepository;
        this.productRepository = productRepository;
    }

    public PersonDetails authenticatePerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authenticating person: {}", authentication.getName());
        return (PersonDetails) authentication.getPrincipal();
    }

    public Product product(int id) {
        log.debug("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException("Product with id " + id + " not found");
                });
    }

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

    public Optional<Person> showPhone(String phone) {
        log.debug("Finding person by phone: {}", phone);
        return peopleRepository.findByPhone(phone).stream().findAny();
    }

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

