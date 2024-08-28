package ru.obozhulkin.Onlineshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.repositories.PeopleRepository;
import ru.obozhulkin.Onlineshop.repositories.ProductRepository;
import ru.obozhulkin.Onlineshop.security.PersonDetails;

import javax.validation.constraints.Min;
import java.util.*;


@Service
public class PersonDetailsService implements UserDetailsService {

private final PeopleRepository peopleRepository;
    private final ProductRepository productRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository, ProductRepository productRepository) {
        this.peopleRepository = peopleRepository;
        this.productRepository = productRepository;
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public Product product(int id){
        Product product=new Product(peopleRepository.findById(id));
        return product;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person=peopleRepository.findByUsername(s);

    if (person.isEmpty())
        throw new UsernameNotFoundException("Пользователь не найден!");

    return new PersonDetails(person.get());
    }
    public Optional<Person> showPhone(String phone){
    return peopleRepository.findByPhone(phone).stream().findAny();
    }

    @Transactional
    public void addBasket(int personId, int productId) {

        Person person=peopleRepository.getById(personId);
        Product product=productRepository.getById(productId);
        int counter= product.getQuantity();
        counter=counter-1;
        product.setQuantity(counter);
        person.addInBasket(product);
        product.addBuyer(person);
    }

    @Transactional
    public void deleteProduct(int personId, int productId){
        Person person=peopleRepository.getById(personId);
        Product product=productRepository.getById(productId);
        product.setQuantity(product.getQuantity()+1);
        person.deleteInBasket(product);
    }

    public List<Product> getProductByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            System.out.println(person.get().getBasket().toString());
            return person.get().getBasket();
        }
        else {
            return Collections.emptyList();
        }
    }
}


