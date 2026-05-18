package com.example.kur.application.service;

import com.example.kur.application.exception.ConflictException;
import com.example.kur.application.exception.NotFoundException;
import com.example.kur.application.port.PasswordHasher;
import com.example.kur.application.port.UnitOfWork;
import com.example.kur.domain.model.Email;
import com.example.kur.domain.model.StudentId;
import com.example.kur.domain.model.UserAccount;
import com.example.kur.domain.model.UserRole;
import com.example.kur.domain.port.StudentRepository;
import com.example.kur.domain.port.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManagementService {
    private static final int GENERATED_PASSWORD_LENGTH = 12;
    private static final String PASSWORD_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;
    private final PasswordHasher passwordHasher;
    private final UnitOfWork unitOfWork;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserManagementService(
            StudentRepository studentRepository,
            UserAccountRepository userAccountRepository,
            PasswordHasher passwordHasher,
            UnitOfWork unitOfWork
    ) {
        this.studentRepository = studentRepository;
        this.userAccountRepository = userAccountRepository;
        this.passwordHasher = passwordHasher;
        this.unitOfWork = unitOfWork;
    }

    public void createAdmin(String email, String rawPassword) {
        unitOfWork.run(() -> {
            Email emailVo = new Email(email);
            if (userAccountRepository.findByEmail(emailVo).isPresent()) {
                throw new ConflictException("User already exists");
            }
            String passwordHash = passwordHasher.hash(rawPassword);
            UserAccount admin = new UserAccount(UUID.randomUUID(), emailVo, passwordHash, UserRole.ADMIN, true);
            userAccountRepository.save(admin);
        });
    }

    public void ensureAdminExists(String email, String rawPassword) {
        unitOfWork.run(() -> {
            Email emailVo = new Email(email);
            if (userAccountRepository.findByEmail(emailVo).isPresent()) {
                return;
            }
            String passwordHash = passwordHasher.hash(rawPassword);
            UserAccount admin = new UserAccount(UUID.randomUUID(), emailVo, passwordHash, UserRole.ADMIN, true);
            userAccountRepository.save(admin);
        });
    }

    public Optional<String> grantStudentAccess(UUID studentId) {
        return unitOfWork.call(() -> {
            var student = studentRepository.findById(new StudentId(studentId))
                    .orElseThrow(() -> new NotFoundException("Student not found"));

            student.setAccess(true);
            studentRepository.save(student);

            Email email = student.email();
            Optional<UserAccount> existing = userAccountRepository.findByEmail(email);
            if (existing.isPresent()) {
                UserAccount account = existing.get();
                account.setRole(UserRole.STUDENT);
                account.setEnabled(true);
                userAccountRepository.save(account);
                return Optional.<String>empty();
            }

            String rawPassword = generatePassword();
            String passwordHash = passwordHasher.hash(rawPassword);
            UserAccount created = new UserAccount(UUID.randomUUID(), email, passwordHash, UserRole.STUDENT, true);
            userAccountRepository.save(created);
            return Optional.of(rawPassword);
        });
    }

    public void revokeStudentAccess(UUID studentId) {
        unitOfWork.run(() -> {
            var student = studentRepository.findById(new StudentId(studentId))
                    .orElseThrow(() -> new NotFoundException("Student not found"));

            student.setAccess(false);
            studentRepository.save(student);

            userAccountRepository.findByEmail(student.email()).ifPresent(account -> {
                account.setEnabled(false);
                userAccountRepository.save(account);
            });
        });
    }

    private String generatePassword() {
        StringBuilder password = new StringBuilder(GENERATED_PASSWORD_LENGTH);
        for (int i = 0; i < GENERATED_PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_ALPHABET.charAt(secureRandom.nextInt(PASSWORD_ALPHABET.length())));
        }
        return password.toString();
    }
}
