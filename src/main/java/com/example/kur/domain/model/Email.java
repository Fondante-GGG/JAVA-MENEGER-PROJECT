package com.example.kur.domain.model;

import com.example.kur.domain.DomainValidationException;

import java.util.Objects;

public record Email(String value) {
    public Email {
        if (value == null) throw new DomainValidationException("email is required");
        String trimmed = value.trim();
        if (trimmed.isBlank()) throw new DomainValidationException("email is required");
        if (trimmed.length() > 320) throw new DomainValidationException("email is too long");
        if (!trimmed.contains("@")) throw new DomainValidationException("email is invalid");
        value = trimmed.toLowerCase();
    }

    @Override
    public String toString() {
        return Objects.toString(value, "");
    }
}

