package com.example.kur.application.port;

public interface PasswordHasher {
    String hash(String rawPassword);
}

