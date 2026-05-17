package com.example.kur.application.service;

import com.example.kur.application.dto.CourseDto;
import com.example.kur.application.exception.NotFoundException;
import com.example.kur.application.port.UnitOfWork;
import com.example.kur.domain.model.Course;
import com.example.kur.domain.model.CourseId;
import com.example.kur.domain.port.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UnitOfWork unitOfWork;

    public CourseService(CourseRepository courseRepository, UnitOfWork unitOfWork) {
        this.courseRepository = courseRepository;
        this.unitOfWork = unitOfWork;
    }

    public List<CourseDto> list() {
        return courseRepository.findAll().stream()
                .map(c -> new CourseDto(c.id().value(), c.title(), c.description()))
                .toList();
    }

    public CourseDto get(UUID id) {
        Course course = courseRepository.findById(new CourseId(id))
                .orElseThrow(() -> new NotFoundException("Course not found"));
        return new CourseDto(course.id().value(), course.title(), course.description());
    }

    public UUID create(String title, String description) {
        return unitOfWork.call(() -> {
            CourseId id = new CourseId(UUID.randomUUID());
            Course course = new Course(id, title, description);
            courseRepository.save(course);
            return id.value();
        });
    }

    public void update(UUID id, String title, String description) {
        unitOfWork.run(() -> {
            Course course = courseRepository.findById(new CourseId(id))
                    .orElseThrow(() -> new NotFoundException("Course not found"));
            course.rename(title);
            course.changeDescription(description);
            courseRepository.save(course);
        });
    }

    public void delete(UUID id) {
        unitOfWork.run(() -> {
            CourseId courseId = new CourseId(id);
            if (!courseRepository.existsById(courseId)) throw new NotFoundException("Course not found");
            courseRepository.deleteById(courseId);
        });
    }
}

