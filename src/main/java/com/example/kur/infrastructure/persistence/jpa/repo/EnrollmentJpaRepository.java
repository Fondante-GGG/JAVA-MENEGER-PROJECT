package com.example.kur.infrastructure.persistence.jpa.repo;

import com.example.kur.infrastructure.persistence.jpa.entity.EnrollmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentJpaEntity, UUID> {
    Optional<EnrollmentJpaEntity> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
}

