package com.example.kur.interfaces.web;

import com.example.kur.application.exception.ConflictException;
import com.example.kur.application.exception.NotFoundException;
import com.example.kur.domain.DomainValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(NotFoundException ex, Model model) {
        model.addAttribute("title", "Not Found");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler({ConflictException.class, DomainValidationException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(RuntimeException ex, Model model) {
        model.addAttribute("title", "Bad Request");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String conflict(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("title", "Conflict");
        model.addAttribute("message", "Operation conflicts with existing data (unique constraint or foreign key).");
        return "error";
    }
}
