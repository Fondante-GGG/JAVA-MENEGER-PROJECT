package com.example.kur.domain.model;

import com.example.kur.domain.DomainValidationException;

public class Course {
    private final CourseId id;
    private String title;
    private String description;

    public Course(CourseId id, String title, String description) {
        this.id = id;
        setTitle(title);
        setDescription(description);
    }

    public CourseId id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public void rename(String newTitle) {
        setTitle(newTitle);
    }

    public void changeDescription(String newDescription) {
        setDescription(newDescription);
    }

    private void setTitle(String value) {
        if (value == null || value.trim().isBlank()) throw new DomainValidationException("course title is required");
        String trimmed = value.trim();
        if (trimmed.length() > 200) throw new DomainValidationException("course title is too long");
        this.title = trimmed;
    }

    private void setDescription(String value) {
        if (value == null) value = "";
        String trimmed = value.trim();
        if (trimmed.length() > 2000) throw new DomainValidationException("course description is too long");
        this.description = trimmed;
    }
}

