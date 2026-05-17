package com.example.kur.domain.model;

import com.example.kur.domain.DomainValidationException;

import java.util.UUID;

public class UserAccount {
    private final UUID id;
    private final Email email;
    private String passwordHash;
    private UserRole role;
    private boolean enabled;

    public UserAccount(UUID id, Email email, String passwordHash, UserRole role, boolean enabled) {
        if (id == null) throw new DomainValidationException("id is required");
        if (email == null) throw new DomainValidationException("email is required");
        setPasswordHash(passwordHash);
        setRole(role);
        this.id = id;
        this.email = email;
        this.enabled = enabled;
    }

    public UUID id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public String passwordHash() {
        return passwordHash;
    }

    public UserRole role() {
        return role;
    }

    public boolean enabled() {
        return enabled;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new DomainValidationException("password hash is required");
        }
        this.passwordHash = passwordHash;
    }

    public void setRole(UserRole role) {
        if (role == null) throw new DomainValidationException("role is required");
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

