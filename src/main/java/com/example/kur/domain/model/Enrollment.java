package com.example.kur.domain.model;

import java.time.Instant;

public class Enrollment {
    private final EnrollmentId id;
    private final StudentId studentId;
    private final CourseId courseId;
    private final Instant enrolledAt;

    public Enrollment(EnrollmentId id, StudentId studentId, CourseId courseId, Instant enrolledAt) {
        if (studentId == null) throw new IllegalArgumentException("studentId is required");
        if (courseId == null) throw new IllegalArgumentException("courseId is required");
        if (enrolledAt == null) throw new IllegalArgumentException("enrolledAt is required");
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt;
    }

    public EnrollmentId id() {
        return id;
    }

    public StudentId studentId() {
        return studentId;
    }

    public CourseId courseId() {
        return courseId;
    }

    public Instant enrolledAt() {
        return enrolledAt;
    }
}

