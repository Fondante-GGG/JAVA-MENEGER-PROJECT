package com.example.kur.domain.port;

import com.example.kur.domain.model.Email;
import com.example.kur.domain.model.Student;
import com.example.kur.domain.model.StudentId;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Optional<Student> findById(StudentId id);

    Optional<Student> findByEmail(Email email);

    List<Student> findAll();

    void save(Student student);

    void deleteById(StudentId id);

    boolean existsById(StudentId id);
}

