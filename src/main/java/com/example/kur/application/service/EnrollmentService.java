package com.example.kur.application.service;

import com.example.kur.application.dto.EnrollmentDto;
import com.example.kur.application.exception.ConflictException;
import com.example.kur.application.exception.NotFoundException;
import com.example.kur.application.port.UnitOfWork;
import com.example.kur.domain.model.Course;
import com.example.kur.domain.model.CourseId;
import com.example.kur.domain.model.Enrollment;
import com.example.kur.domain.model.EnrollmentId;
import com.example.kur.domain.model.Student;
import com.example.kur.domain.model.StudentId;
import com.example.kur.domain.port.CourseRepository;
import com.example.kur.domain.port.EnrollmentRepository;
import com.example.kur.domain.port.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UnitOfWork unitOfWork;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            UnitOfWork unitOfWork
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.unitOfWork = unitOfWork;
    }

    public List<EnrollmentDto> list() {
        Map<UUID, Student> studentsById = studentRepository.findAll().stream()
                .collect(Collectors.toMap(s -> s.id().value(), Function.identity()));
        Map<UUID, Course> coursesById = courseRepository.findAll().stream()
                .collect(Collectors.toMap(c -> c.id().value(), Function.identity()));

        return enrollmentRepository.findAll().stream()
                .map(e -> {
                    Student s = studentsById.get(e.studentId().value());
                    Course c = coursesById.get(e.courseId().value());
                    return new EnrollmentDto(
                            e.id().value(),
                            e.studentId().value(),
                            s != null ? s.fullName() : "(deleted)",
                            s != null ? s.email().value() : "",
                            e.courseId().value(),
                            c != null ? c.title() : "(deleted)",
                            e.enrolledAt()
                    );
                })
                .toList();
    }

    public UUID enroll(UUID studentId, UUID courseId) {
        return unitOfWork.call(() -> {
            StudentId sId = new StudentId(studentId);
            CourseId cId = new CourseId(courseId);

            if (!studentRepository.existsById(sId)) throw new NotFoundException("Student not found");
            if (!courseRepository.existsById(cId)) throw new NotFoundException("Course not found");
            if (enrollmentRepository.findByStudentAndCourse(sId, cId).isPresent()) {
                throw new ConflictException("Student is already enrolled in this course");
            }

            EnrollmentId id = new EnrollmentId(UUID.randomUUID());
            Enrollment enrollment = new Enrollment(id, sId, cId, Instant.now());
            enrollmentRepository.save(enrollment);
            return id.value();
        });
    }

    public void delete(UUID id) {
        unitOfWork.run(() -> {
            EnrollmentId enrollmentId = new EnrollmentId(id);
            if (!enrollmentRepository.existsById(enrollmentId)) throw new NotFoundException("Enrollment not found");
            enrollmentRepository.deleteById(enrollmentId);
        });
    }
}

