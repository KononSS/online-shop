package ru.obozhulkin.Onlineshop.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.obozhulkin.Onlineshop.models.Person;
import java.util.Collection;
import java.util.Collections;

/**
 * Класс, реализующий интерфейс UserDetails для представления деталей пользователя.
 */
public class PersonDetails implements UserDetails {

    /** Пользователь. */
    private Person person;

    public PersonDetails(){
    }

    /**
     * Конструктор для инициализации пользователя.
     *
     * @param person Пользователь.
     */
    public PersonDetails(Person person) {
        this.person = person;
    }

    /**
     * Возвращает роли пользователя.
     *
     * @return Коллекция ролей пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return Имя пользователя.
     */
    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта пользователя.
     *
     * @return true, если аккаунт не истек.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт пользователя.
     *
     * @return true, если аккаунт не заблокирован.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных пользователя.
     *
     * @return true, если учетные данные не истекли.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, включен ли аккаунт пользователя.
     *
     * @return true, если аккаунт включен.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Возвращает пользователя.
     *
     * @return Пользователь.
     */
    public Person getPerson() {
        return this.person;
    }
}
