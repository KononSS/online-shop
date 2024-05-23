package ru.obozhulkin.Onlineshop.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;


@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;
@Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return  Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
Person person=(Person)o;
if(personDetailsService.showPhone(person.getPhone()).isPresent())
    errors.rejectValue("phone",""," Человек с таким номером уже зарегистрирован");
try {
    personDetailsService.loadUserByUsername(person.getUsername());
}catch (UsernameNotFoundException ignored){
    return;
}
errors.rejectValue("username",""," Человек с таким именем уже зарегистрирован");
}
}
