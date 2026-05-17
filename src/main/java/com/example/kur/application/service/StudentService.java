package com.example.kur.application.service;

import com.example.kur.application.dto.StudentDto;
import com.example.kur.application.exception.ConflictException;
import com.example.kur.application.exception.NotFoundException;
import com.example.kur.application.port.UnitOfWork;
import com.example.kur.domain.model.Email;
import com.example.kur.domain.model.Student;
import com.example.kur.domain.model.StudentId;
import com.example.kur.domain.port.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UnitOfWork unitOfWork;

    public StudentService(StudentRepository studentRepository, UnitOfWork unitOfWork) {
        this.studentRepository = studentRepository;
        this.unitOfWork = unitOfWork;
    }

    public List<StudentDto> list() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentDto(s.id().value(), s.fullName(), s.email().value(), s.hasAccess()))
                .toList();
    }

    public StudentDto get(UUID id) {
        Student student = studentRepository.findById(new StudentId(id))
                .orElseThrow(() -> new NotFoundException("Student not found"));
        return new StudentDto(student.id().value(), student.fullName(), student.email().value(), student.hasAccess());
    }

    public UUID create(String fullName, String email) {
        return unitOfWork.call(() -> {
            Email emailVo = new Email(email);
            if (studentRepository.findByEmail(emailVo).isPresent()) {
                throw new ConflictException("Email already exists");
            }
            StudentId id = new StudentId(UUID.randomUUID());
            Student student = new Student(id, fullName, emailVo, false);
            studentRepository.save(student);
            return id.value();
        });
    }

    public void update(UUID id, String fullName, String email) {
        unitOfWork.run(() -> {
            Student student = studentRepository.findById(new StudentId(id))
                    .orElseThrow(() -> new NotFoundException("Student not found"));
            Email emailVo = new Email(email);
            studentRepository.findByEmail(emailVo)
                    .filter(other -> !other.id().value().equals(id))
                    .ifPresent(other -> {
                        throw new ConflictException("Email already exists");
                    });
            student.rename(fullName);
            student.changeEmail(emailVo);
            studentRepository.save(student);
        });
    }

    public void setAccess(UUID id, boolean hasAccess) {
        unitOfWork.run(() -> {
            Student student = studentRepository.findById(new StudentId(id))
                    .orElseThrow(() -> new NotFoundException("Student not found"));
            student.setAccess(hasAccess);
            studentRepository.save(student);
        });
    }

    public void delete(UUID id) {
        unitOfWork.run(() -> {
            StudentId studentId = new StudentId(id);
            if (!studentRepository.existsById(studentId)) throw new NotFoundException("Student not found");
            studentRepository.deleteById(studentId);
        });
    }
}
