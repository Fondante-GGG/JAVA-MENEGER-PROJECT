package com.example.kur.domain.port;

import com.example.kur.domain.model.Course;
import com.example.kur.domain.model.CourseId;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Optional<Course> findById(CourseId id);

    List<Course> findAll();

    void save(Course course);

    void deleteById(CourseId id);

    boolean existsById(CourseId id);
}

