package ru.obozhulkin.Onlineshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.models.Product;
import ru.obozhulkin.Onlineshop.repositories.PeopleRepository;
import ru.obozhulkin.Onlineshop.repositories.ProductRepository;
import ru.obozhulkin.Onlineshop.security.PersonDetails;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class PersonDetailsService implements UserDetailsService {

private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
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

    public List<Product> getProductByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {

            System.out.println(person.get().getBasket().toString());
//            Hibernate.initialize(person.get().getBasket());
            return person.get().getBasket();
        }
        else {
            return Collections.emptyList();
        }
    }
}


