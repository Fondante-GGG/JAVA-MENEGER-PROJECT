package com.example.kur.infrastructure.persistence.adapter;

import com.example.kur.domain.model.CourseId;
import com.example.kur.domain.model.Enrollment;
import com.example.kur.domain.model.EnrollmentId;
import com.example.kur.domain.model.StudentId;
import com.example.kur.domain.port.EnrollmentRepository;
import com.example.kur.infrastructure.persistence.jpa.entity.EnrollmentJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.repo.EnrollmentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentRepositoryAdapter implements EnrollmentRepository {
    private final EnrollmentJpaRepository repo;

    public EnrollmentRepositoryAdapter(EnrollmentJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<Enrollment> findById(EnrollmentId id) {
        return repo.findById(id.value()).map(this::toDomain);
    }

    @Override
    public Optional<Enrollment> findByStudentAndCourse(StudentId studentId, CourseId courseId) {
        return repo.findByStudentIdAndCourseId(studentId.value(), courseId.value()).map(this::toDomain);
    }

    @Override
    public List<Enrollment> findAll() {
        return repo.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void save(Enrollment enrollment) {
        repo.save(toJpa(enrollment));
    }

    @Override
    public void deleteById(EnrollmentId id) {
        repo.deleteById(id.value());
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        return repo.existsById(id.value());
    }

    private Enrollment toDomain(EnrollmentJpaEntity e) {
        return new Enrollment(
                new EnrollmentId(e.getId()),
                new StudentId(e.getStudentId()),
                new CourseId(e.getCourseId()),
                e.getEnrolledAt()
        );
    }

    private EnrollmentJpaEntity toJpa(Enrollment e) {
        return new EnrollmentJpaEntity(
                e.id().value(),
                e.studentId().value(),
                e.courseId().value(),
                e.enrolledAt()
        );
    }
}

