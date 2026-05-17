package com.example.kur.domain.model;

import com.example.kur.domain.DomainValidationException;

public class Student {
    private final StudentId id;
    private String fullName;
    private Email email;
    private boolean hasAccess;

    public Student(StudentId id, String fullName, Email email, boolean hasAccess) {
        this.id = id;
        setFullName(fullName);
        changeEmail(email);
        this.hasAccess = hasAccess;
    }

    public StudentId id() {
        return id;
    }

    public String fullName() {
        return fullName;
    }

    public Email email() {
        return email;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public void rename(String newName) {
        setFullName(newName);
    }

    public void changeEmail(Email newEmail) {
        if (newEmail == null) throw new DomainValidationException("email is required");
        this.email = newEmail;
    }

    private void setFullName(String value) {
        if (value == null || value.trim().isBlank()) throw new DomainValidationException("full name is required");
        String trimmed = value.trim();
        if (trimmed.length() > 200) throw new DomainValidationException("full name is too long");
        this.fullName = trimmed;
    }
}
