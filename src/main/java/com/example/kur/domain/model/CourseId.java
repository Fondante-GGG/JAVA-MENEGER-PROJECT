package com.example.kur.domain.model;

import java.util.UUID;

public record CourseId(UUID value) {
    public CourseId {
        if (value == null) throw new IllegalArgumentException("courseId is required");
    }
}

