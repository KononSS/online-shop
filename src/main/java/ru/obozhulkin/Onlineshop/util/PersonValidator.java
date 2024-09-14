package ru.obozhulkin.Onlineshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.services.PersonDetailsService;

/**
 * Валидатор для пользователей.
 */
@Slf4j
@Component
public class PersonValidator implements Validator {

    /** Сервис для работы с пользователями. */
    private final PersonDetailsService personDetailsService;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param personDetailsService Сервис для работы с деталями пользователя.
     */
    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    /**
     * Проверяет, поддерживается ли валидация для указанного класса.
     *
     * @param aClass Класс для проверки.
     * @return true, если валидация поддерживается, иначе false.
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    /**
     * Валидирует объект пользователя.
     *
     * @param o Объект для валидации.
     * @param errors Объект для хранения ошибок валидации.
     */
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
