package ru.obozhulkin.Onlineshop.security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.obozhulkin.Onlineshop.models.Person;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDetailsTest {

    private Person person;
    private PersonDetails personDetails;

    @BeforeEach
    public void setUp() {
        person = new Person();
        person.setUsername("testUser");
        person.setPassword("testPassword");
        person.setRole("ROLE_USER");

        personDetails = new PersonDetails(person);
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = personDetails.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testGetPassword() {
        assertEquals("testPassword", personDetails.getPassword());
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", personDetails.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(personDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(personDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(personDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(personDetails.isEnabled());
    }

    @Test
    public void testGetPerson() {
        assertEquals(person, personDetails.getPerson());
    }
}