package ru.obozhulkin.Onlineshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.obozhulkin.Onlineshop.exeption.RegistrationException;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.repositories.PeopleRepository;
import java.time.LocalDateTime;

@Slf4j
@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        log.debug("Registering new user: {}", person);
        try {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            person.setRole("ROLE_USER");
            person.setCreatedAt(LocalDateTime.now());
            peopleRepository.save(person);
            log.debug("User registered successfully: {}", person);
        } catch (Exception e) {
            log.error("Failed to register user: {}", e.getMessage(), e);
            throw new RegistrationException("Failed to register user: " + e.getMessage(), e);
        }
    }
}