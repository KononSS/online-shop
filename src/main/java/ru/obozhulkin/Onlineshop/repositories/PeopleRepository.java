package ru.obozhulkin.Onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.obozhulkin.Onlineshop.models.Person;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {

    Optional<Person> findByPhone(String phone);
    Optional<Person> findByUsername(String username);
}
