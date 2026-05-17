package com.example.kur.interfaces.cli;

import com.example.kur.application.service.UserManagementService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

@Component
public class CreateAdminCommandRunner implements ApplicationRunner {
    private static final int GENERATED_PASSWORD_LENGTH = 16;
    private static final String PASSWORD_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

    private final UserManagementService userManagementService;
    private final ConfigurableApplicationContext applicationContext;
    private final SecureRandom secureRandom = new SecureRandom();

    public CreateAdminCommandRunner(
            UserManagementService userManagementService,
            ConfigurableApplicationContext applicationContext
    ) {
        this.userManagementService = userManagementService;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> nonOptionArgs = args.getNonOptionArgs();
        if (nonOptionArgs.isEmpty() || !"create-admin".equals(nonOptionArgs.get(0))) {
            return;
        }

        String email = optionValue(args, "email");
        if (email == null || email.isBlank()) {
            System.err.println("Missing required option: --email");
            exit(2);
            return;
        }

        String password = optionValue(args, "password");
        if (password == null || password.isBlank()) {
            password = generatePassword();
            System.out.println("Generated admin password: " + password);
        }

        try {
            userManagementService.createAdmin(email, password);
            System.out.println("Admin created: " + email);
            exit(0);
        } catch (Exception e) {
            System.err.println("Failed to create admin: " + e.getMessage());
            exit(1);
        }
    }

    private String optionValue(ApplicationArguments args, String name) {
        List<String> values = args.getOptionValues(name);
        if (values == null || values.isEmpty()) return null;
        return values.get(0);
    }

    private String generatePassword() {
        StringBuilder password = new StringBuilder(GENERATED_PASSWORD_LENGTH);
        for (int i = 0; i < GENERATED_PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_ALPHABET.charAt(secureRandom.nextInt(PASSWORD_ALPHABET.length())));
        }
        return password.toString();
    }

    private void exit(int code) {
        int exitCode = org.springframework.boot.SpringApplication.exit(applicationContext, () -> code);
        System.exit(exitCode);
    }
}
