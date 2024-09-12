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

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String loginPage() {
        log.info("Accessed /auth/login endpoint");
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") PersonDTO personDTO) {
        log.info("Accessed /auth/registration GET endpoint");
        return "auth/registration";
    }

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