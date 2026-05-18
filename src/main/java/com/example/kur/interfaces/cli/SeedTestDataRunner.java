package com.example.kur.interfaces.cli;

import com.example.kur.domain.model.UserRole;
import com.example.kur.infrastructure.persistence.jpa.entity.CourseJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.entity.EnrollmentJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.entity.StudentJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.repo.CourseJpaRepository;
import com.example.kur.infrastructure.persistence.jpa.repo.EnrollmentJpaRepository;
import com.example.kur.infrastructure.persistence.jpa.repo.StudentJpaRepository;
import com.example.kur.infrastructure.persistence.jpa.repo.UserJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class SeedTestDataRunner implements ApplicationRunner {
    private final CourseJpaRepository courseRepository;
    private final StudentJpaRepository studentRepository;
    private final EnrollmentJpaRepository enrollmentRepository;
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;

    public SeedTestDataRunner(
            CourseJpaRepository courseRepository,
            StudentJpaRepository studentRepository,
            EnrollmentJpaRepository enrollmentRepository,
            UserJpaRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed-test-data:true}") boolean enabled
    ) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<String> nonOptionArgs = args.getNonOptionArgs();
        if (!enabled || (!nonOptionArgs.isEmpty() && "create-admin".equals(nonOptionArgs.get(0)))) {
            return;
        }

        Map<String, CourseJpaEntity> coursesByTitle = new HashMap<>();
        for (CourseSeed seed : courseSeeds()) {
            CourseJpaEntity course = courseRepository.findAll().stream()
                    .filter(existing -> existing.getTitle().equalsIgnoreCase(seed.title()))
                    .findFirst()
                    .orElseGet(() -> courseRepository.save(new CourseJpaEntity(UUID.randomUUID(), seed.title(), seed.description())));
            coursesByTitle.put(seed.title(), course);
        }

        Map<String, StudentJpaEntity> studentsByEmail = new HashMap<>();
        for (StudentSeed seed : studentSeeds()) {
            StudentJpaEntity student = studentRepository.findByEmail(seed.email())
                    .map(existing -> {
                        existing.setFullName(seed.fullName());
                        existing.setHasAccess(seed.hasAccess());
                        return studentRepository.save(existing);
                    })
                    .orElseGet(() -> studentRepository.save(
                            new StudentJpaEntity(UUID.randomUUID(), seed.fullName(), seed.email(), seed.hasAccess())
                    ));
            studentsByEmail.put(seed.email(), student);

            if (seed.hasAccess()) {
                ensureUser(seed.email(), seed.password(), UserRole.STUDENT, true);
            }
        }

        for (EnrollmentSeed seed : enrollmentSeeds()) {
            StudentJpaEntity student = studentsByEmail.get(seed.studentEmail());
            CourseJpaEntity course = coursesByTitle.get(seed.courseTitle());
            if (student == null || course == null) {
                continue;
            }
            enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId())
                    .orElseGet(() -> enrollmentRepository.save(new EnrollmentJpaEntity(
                            UUID.randomUUID(),
                            student.getId(),
                            course.getId(),
                            Instant.now()
                    )));
        }
    }

    private void ensureUser(String email, String rawPassword, UserRole role, boolean enabled) {
        userRepository.findByEmail(email)
                .map(existing -> {
                    existing.setPasswordHash(passwordEncoder.encode(rawPassword));
                    existing.setRole(role);
                    existing.setEnabled(enabled);
                    return userRepository.save(existing);
                })
                .orElseGet(() -> userRepository.save(new UserJpaEntity(
                        UUID.randomUUID(),
                        email,
                        passwordEncoder.encode(rawPassword),
                        role,
                        enabled
                )));
    }

    private List<CourseSeed> courseSeeds() {
        return List.of(
                new CourseSeed("Java Basics", "Variables, methods, classes, and OOP basics."),
                new CourseSeed("Spring Boot API", "Controllers, services, repositories, and REST design."),
                new CourseSeed("Databases with SQLite", "SQL fundamentals, schema design, and persistence."),
                new CourseSeed("Algorithms and Data Structures", "Arrays, lists, trees, graphs, and complexity basics."),
                new CourseSeed("Java Concurrency", "Threads, executors, synchronization, and concurrent collections."),
                new CourseSeed("Hibernate ORM", "Entity mapping, relationships, fetching strategies, and transactions."),
                new CourseSeed("Microservices Fundamentals", "Service boundaries, communication, resilience, and deployment basics."),
                new CourseSeed("Testing with JUnit", "Unit, integration, and web-layer testing for Java applications."),
                new CourseSeed("Maven Build Tools", "Project structure, dependency management, plugins, and lifecycles."),
                new CourseSeed("Git and Collaboration", "Branching, pull requests, code review, and release workflows."),
                new CourseSeed("Web Security Basics", "Authentication, authorization, common attacks, and secure defaults."),
                new CourseSeed("RESTful Service Design", "Resource modeling, HTTP semantics, versioning, and error handling."),
                new CourseSeed("Frontend for Backend Devs", "HTML, CSS, forms, and template rendering fundamentals."),
                new CourseSeed("Docker Essentials", "Containers, images, volumes, networking, and local orchestration."),
                new CourseSeed("Linux for Developers", "Shell usage, processes, filesystems, permissions, and automation."),
                new CourseSeed("Software Architecture Patterns", "Layered, hexagonal, event-driven, and modular system design."),
                new CourseSeed("Clean Code Practices", "Naming, refactoring, readability, and maintainable design habits."),
                new CourseSeed("Design Patterns in Java", "Practical use of creational, structural, and behavioral patterns."),
                new CourseSeed("CI/CD Pipelines", "Build automation, testing gates, and deployment pipeline concepts."),
                new CourseSeed("NoSQL Overview", "Document, key-value, and columnar databases with tradeoff analysis."),
                new CourseSeed("Messaging with Kafka", "Topics, producers, consumers, and event streaming basics."),
                new CourseSeed("Cloud Deployment Basics", "Runtime environments, configuration, logging, and scaling fundamentals.")
        );
    }

    private List<StudentSeed> studentSeeds() {
        return List.of(
                new StudentSeed("Ainura Sadykova", "ainura@example.com", true, "Student12345"),
                new StudentSeed("Bekzat Toktosunov", "bekzat@example.com", true, "Student12345"),
                new StudentSeed("Elina Asanova", "elina@example.com", false, "Student12345"),
                new StudentSeed("Nurlan Ismailov", "nurlan@example.com", true, "Student12345"),
                new StudentSeed("Aizada Joldosheva", "aizada@example.com", true, "Student12345"),
                new StudentSeed("Temirlan Abdraimov", "temirlan@example.com", false, "Student12345"),
                new StudentSeed("Madina Ermekova", "madina@example.com", true, "Student12345"),
                new StudentSeed("Adilet Suyunbaev", "adilet@example.com", true, "Student12345"),
                new StudentSeed("Meerim Osmonalieva", "meerim@example.com", false, "Student12345"),
                new StudentSeed("Ruslan Kadyrov", "ruslan@example.com", true, "Student12345"),
                new StudentSeed("Nazgul Turgunova", "nazgul@example.com", true, "Student12345"),
                new StudentSeed("Azamat Kenzhebaev", "azamat@example.com", false, "Student12345"),
                new StudentSeed("Bermet Asylbekova", "bermet@example.com", true, "Student12345"),
                new StudentSeed("Bakyt Umetaliev", "bakyt@example.com", true, "Student12345"),
                new StudentSeed("Kanykei Nasyrova", "kanykei@example.com", false, "Student12345"),
                new StudentSeed("Ulan Doolotov", "ulan@example.com", true, "Student12345"),
                new StudentSeed("Cholpon Saparova", "cholpon@example.com", true, "Student12345"),
                new StudentSeed("Dastan Ryskulov", "dastan@example.com", false, "Student12345"),
                new StudentSeed("Janyl Mambetova", "janyl@example.com", true, "Student12345"),
                new StudentSeed("Tilek Mamytov", "tilek@example.com", true, "Student12345"),
                new StudentSeed("Altynai Jeenbekova", "altynai@example.com", false, "Student12345")
        );
    }

    private List<EnrollmentSeed> enrollmentSeeds() {
        return List.of(
                new EnrollmentSeed("ainura@example.com", "Java Basics"),
                new EnrollmentSeed("ainura@example.com", "Spring Boot API"),
                new EnrollmentSeed("bekzat@example.com", "Databases with SQLite"),
                new EnrollmentSeed("nurlan@example.com", "Algorithms and Data Structures"),
                new EnrollmentSeed("aizada@example.com", "Java Concurrency"),
                new EnrollmentSeed("temirlan@example.com", "Hibernate ORM"),
                new EnrollmentSeed("madina@example.com", "Microservices Fundamentals"),
                new EnrollmentSeed("adilet@example.com", "Testing with JUnit"),
                new EnrollmentSeed("meerim@example.com", "Maven Build Tools"),
                new EnrollmentSeed("ruslan@example.com", "Git and Collaboration"),
                new EnrollmentSeed("nazgul@example.com", "Web Security Basics"),
                new EnrollmentSeed("azamat@example.com", "RESTful Service Design"),
                new EnrollmentSeed("bermet@example.com", "Frontend for Backend Devs"),
                new EnrollmentSeed("bakyt@example.com", "Docker Essentials"),
                new EnrollmentSeed("kanykei@example.com", "Linux for Developers"),
                new EnrollmentSeed("ulan@example.com", "Software Architecture Patterns"),
                new EnrollmentSeed("cholpon@example.com", "Clean Code Practices"),
                new EnrollmentSeed("dastan@example.com", "Design Patterns in Java"),
                new EnrollmentSeed("janyl@example.com", "CI/CD Pipelines"),
                new EnrollmentSeed("tilek@example.com", "NoSQL Overview"),
                new EnrollmentSeed("altynai@example.com", "Messaging with Kafka"),
                new EnrollmentSeed("ainura@example.com", "Cloud Deployment Basics")
        );
    }

    private record CourseSeed(String title, String description) {}
    private record StudentSeed(String fullName, String email, boolean hasAccess, String password) {}
    private record EnrollmentSeed(String studentEmail, String courseTitle) {}
}
