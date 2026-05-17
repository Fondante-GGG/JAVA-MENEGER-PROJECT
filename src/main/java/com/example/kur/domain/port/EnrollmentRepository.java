package com.example.kur.domain.port;

import com.example.kur.domain.model.CourseId;
import com.example.kur.domain.model.Enrollment;
import com.example.kur.domain.model.EnrollmentId;
import com.example.kur.domain.model.StudentId;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    Optional<Enrollment> findById(EnrollmentId id);

    Optional<Enrollment> findByStudentAndCourse(StudentId studentId, CourseId courseId);

    List<Enrollment> findAll();

    void save(Enrollment enrollment);

    void deleteById(EnrollmentId id);

    boolean existsById(EnrollmentId id);
}

