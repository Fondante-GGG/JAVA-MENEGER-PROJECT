package com.example.kur.infrastructure.persistence.jpa.repo;

import com.example.kur.infrastructure.persistence.jpa.entity.CourseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, UUID> {}

