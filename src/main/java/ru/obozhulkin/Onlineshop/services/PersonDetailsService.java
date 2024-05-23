package ru.obozhulkin.Onlineshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.repositories.PeopleRepository;
import ru.obozhulkin.Onlineshop.security.PersonDetails;

import java.util.Optional;


@Service
public class PersonDetailsService implements UserDetailsService {

private final PeopleRepository peopleRepository;

@Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
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
