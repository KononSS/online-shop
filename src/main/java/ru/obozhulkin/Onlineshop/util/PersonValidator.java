package ru.obozhulkin.Onlineshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;

@Slf4j
@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        log.debug("Validating person: {}", person);

        if (personDetailsService.showPhone(person.getPhone()).isPresent()) {
            log.warn("Person with phone number {} already exists", person.getPhone());
            errors.rejectValue("phone", "", "Человек с таким номером уже зарегистрирован");
        }

        try {
            personDetailsService.loadUserByUsername(person.getUsername());
            log.warn("Person with username {} already exists", person.getUsername());
            errors.rejectValue("username", "", "Человек с таким именем уже зарегистрирован");
        } catch (UsernameNotFoundException ignored) {
            log.debug("Username {} is available", person.getUsername());
        }

        log.debug("Validation completed for person: {}", person);
    }
}
