package com.example.kur.domain.model;

import java.util.UUID;

public record StudentId(UUID value) {
    public StudentId {
        if (value == null) throw new IllegalArgumentException("studentId is required");
    }
}

