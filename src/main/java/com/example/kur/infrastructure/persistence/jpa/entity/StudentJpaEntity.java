package com.example.kur.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "students")
public class StudentJpaEntity {
    @Id
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(nullable = false, length = 320, unique = true)
    private String email;

    @Column(name = "has_access", nullable = false)
    private boolean hasAccess;

    protected StudentJpaEntity() {}

    public StudentJpaEntity(UUID id, String fullName, String email, boolean hasAccess) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.hasAccess = hasAccess;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
