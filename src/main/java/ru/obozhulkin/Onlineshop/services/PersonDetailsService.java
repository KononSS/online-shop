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
import ru.obozhulkin.Onlineshop.security.PersonDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class PersonDetailsService implements UserDetailsService {

private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional
    public void addBasket(int idProduct, Person buyer) {
        Person person=new Person();
        person.setBasket(new ArrayList<>(Collections.singletonList(product(idProduct))));
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
}

