package ru.obozhulkin.Onlineshop.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {
        log.error("An error occurred: ", ex);
        model.addAttribute("errorMessage", "An error occurred: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIOException(IOException ex, Model model) {
        log.error("File upload error: ", ex);
        model.addAttribute("errorMessage", "File upload error: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFoundException(ProductNotFoundException ex, Model model) {
        log.error("Product not found: ", ex);
        model.addAttribute("errorMessage", "Product not found: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePersonNotFoundException(PersonNotFoundException ex, Model model) {
        log.error("Person not found: ", ex);
        model.addAttribute("errorMessage", "Person not found: " + ex.getMessage());
        return "error";
    }
}