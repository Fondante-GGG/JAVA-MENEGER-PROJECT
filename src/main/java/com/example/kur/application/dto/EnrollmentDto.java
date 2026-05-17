package com.example.kur.application.dto;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentDto(
        UUID id,
        UUID studentId,
        String studentName,
        String studentEmail,
        UUID courseId,
        String courseTitle,
        Instant enrolledAt
) {}

