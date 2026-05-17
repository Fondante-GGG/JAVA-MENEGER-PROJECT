package com.example.kur.infrastructure.persistence.adapter;

import com.example.kur.domain.model.Course;
import com.example.kur.domain.model.CourseId;
import com.example.kur.domain.port.CourseRepository;
import com.example.kur.infrastructure.persistence.jpa.entity.CourseJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.repo.CourseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepositoryAdapter implements CourseRepository {
    private final CourseJpaRepository repo;

    public CourseRepositoryAdapter(CourseJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Course> findById(CourseId id) {
        return repo.findById(id.value()).map(this::toDomain);
    }

    @Override
    public List<Course> findAll() {
        return repo.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void save(Course course) {
        repo.save(toJpa(course));
    }

    @Override
    public void deleteById(CourseId id) {
        repo.deleteById(id.value());
    }

    @Override
    public boolean existsById(CourseId id) {
        return repo.existsById(id.value());
    }

    private Course toDomain(CourseJpaEntity e) {
        return new Course(new CourseId(e.getId()), e.getTitle(), e.getDescription());
    }

    private CourseJpaEntity toJpa(Course course) {
        return new CourseJpaEntity(course.id().value(), course.title(), course.description());
    }
}

