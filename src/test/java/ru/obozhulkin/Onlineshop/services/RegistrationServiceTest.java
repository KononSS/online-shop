package ru.obozhulkin.Onlineshop.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.repositories.PeopleRepository;
import ru.obozhulkin.Onlineshop.exeption.RegistrationException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setUsername("testUser");
        person.setPassword("testPassword");
    }

    @Test
    public void testRegisterSuccess() {
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        registrationService.register(person);

        verify(peopleRepository, times(1)).save(person);
        assertEquals("encodedPassword", person.getPassword());
        assertEquals("ROLE_USER", person.getRole());
        assertNotNull(person.getCreatedAt());
    }

    @Test
    public void testRegisterFailure() {
        when(passwordEncoder.encode("testPassword")).thenThrow(new RuntimeException("Encoding error"));

        assertThrows(RegistrationException.class, () -> {
            registrationService.register(person);
        });

        verify(peopleRepository, never()).save(any(Person.class));
    }
}