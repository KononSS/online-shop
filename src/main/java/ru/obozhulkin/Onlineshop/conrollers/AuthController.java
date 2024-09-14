package ru.obozhulkin.Onlineshop.conrollers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.obozhulkin.Onlineshop.DTO.PersonDTO;
import ru.obozhulkin.Onlineshop.models.Person;
import ru.obozhulkin.Onlineshop.services.RegistrationService;
import ru.obozhulkin.Onlineshop.util.PersonValidator;
import javax.validation.Valid;

/**
 * Контроллер для обработки аутентификации и регистрации пользователей.
 */
@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {

    /** Сервис для регистрации пользователей. */
    private final RegistrationService registrationService;
    /** Валидатор для пользователей. */
    private final PersonValidator personValidator;
    /** Маппер для преобразования объектов. */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param registrationService Сервис для регистрации пользователей.
     * @param personValidator Валидатор для пользователей.
     * @param modelMapper Маппер для преобразования объектов.
     */
    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
    }

    /**
     * Обрабатывает GET-запрос на страницу входа.
     *
     * @return Имя представления для страницы входа.
     */
    @GetMapping("/login")
    public String loginPage() {
        log.info("Accessed /auth/login endpoint");
        return "auth/login";
    }

    /**
     * Обрабатывает GET-запрос на страницу регистрации.
     *
     * @param personDTO Объект DTO для пользователя.
     * @return Имя представления для страницы регистрации.
     */
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") PersonDTO personDTO) {
        log.info("Accessed /auth/registration GET endpoint");
        return "auth/registration";
    }

    /**
     * Обрабатывает POST-запрос на регистрацию пользователя.
     *
     * @param personDTO Объект DTO для пользователя.
     * @param bindingResult Результат валидации.
     * @return Имя представления для страницы входа или страницы регистрации в случае ошибки.
     */
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult) {
        log.info("Accessed /auth/registration POST endpoint");

        personValidator.validate(modelMapper.map(personDTO, Person.class), bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors occurred while registering person");
            return "auth/registration";
        }
        try {
            registrationService.register(modelMapper.map(personDTO, Person.class));
            log.info("Person registered successfully: {}", personDTO.getUsername());
        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());
            bindingResult.reject("registration.error", "Registration error: " + e.getMessage());
            return "auth/registration";
        }
        return "redirect:/auth/login";
    }
}