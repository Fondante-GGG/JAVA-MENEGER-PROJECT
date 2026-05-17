package com.example.kur.infrastructure.persistence.jpa.repo;

import com.example.kur.infrastructure.persistence.jpa.entity.StudentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, UUID> {
    Optional<StudentJpaEntity> findByEmail(String email);
}

