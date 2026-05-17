package com.example.kur.domain.model;

import java.util.UUID;

public record EnrollmentId(UUID value) {
    public EnrollmentId {
        if (value == null) throw new IllegalArgumentException("enrollmentId is required");
    }
}

