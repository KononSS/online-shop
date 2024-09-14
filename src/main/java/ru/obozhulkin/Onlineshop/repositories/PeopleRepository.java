package ru.obozhulkin.Onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.obozhulkin.Onlineshop.models.Person;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью Person.
 */
@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    /**
     * Находит пользователя по номеру телефона.
     *
     * @param phone Номер телефона пользователя.
     * @return Optional с найденным пользователем.
     */
    Optional<Person> findByPhone(String phone);

    /**
     * Находит пользователя по имени.
     *
     * @param username Имя пользователя.
     * @return Optional с найденным пользователем.
     */
    Optional<Person> findByUsername(String username);
}