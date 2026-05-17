package com.example.kur.infrastructure.persistence.adapter;

import com.example.kur.domain.model.Email;
import com.example.kur.domain.model.Student;
import com.example.kur.domain.model.StudentId;
import com.example.kur.domain.port.StudentRepository;
import com.example.kur.infrastructure.persistence.jpa.entity.StudentJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.repo.StudentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryAdapter implements StudentRepository {
    private final StudentJpaRepository repo;

    public StudentRepositoryAdapter(StudentJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Student> findById(StudentId id) {
        return repo.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Student> findByEmail(Email email) {
        return repo.findByEmail(email.value()).map(this::toDomain);
    }

    @Override
    public List<Student> findAll() {
        return repo.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void save(Student student) {
        repo.save(toJpa(student));
    }

    @Override
    public void deleteById(StudentId id) {
        repo.deleteById(id.value());
    }

    @Override
    public boolean existsById(StudentId id) {
        return repo.existsById(id.value());
    }

    private Student toDomain(StudentJpaEntity e) {
        return new Student(new StudentId(e.getId()), e.getFullName(), new Email(e.getEmail()), e.isHasAccess());
    }

    private StudentJpaEntity toJpa(Student s) {
        return new StudentJpaEntity(s.id().value(), s.fullName(), s.email().value(), s.hasAccess());
    }
}
