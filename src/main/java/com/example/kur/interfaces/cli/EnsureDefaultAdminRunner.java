package com.example.kur.interfaces.cli;

import com.example.kur.application.service.UserManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnsureDefaultAdminRunner implements ApplicationRunner {
    private final UserManagementService userManagementService;
    private final String email;
    private final String password;

    public EnsureDefaultAdminRunner(
            UserManagementService userManagementService,
            @Value("${app.default-admin.email:admin@example.com}") String email,
            @Value("${app.default-admin.password:Admin12345}") String password
    ) {
        this.userManagementService = userManagementService;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> nonOptionArgs = args.getNonOptionArgs();
        if (!nonOptionArgs.isEmpty() && "create-admin".equals(nonOptionArgs.get(0))) {
            return;
        }

        userManagementService.ensureAdminExists(email, password);
    }
}
