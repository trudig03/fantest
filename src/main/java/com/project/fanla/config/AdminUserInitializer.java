package com.project.fanla.config;

import com.project.fanla.entity.User;
import com.project.fanla.enums.Role;
import com.project.fanla.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.firstName}")
    private String adminFirstName;

    @Value("${admin.lastName}")
    private String adminLastName;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if superadmin already exists
        if (!userRepository.existsByUsername(adminUsername)) {
            // Create new superadmin user
            User superAdmin = new User();
            superAdmin.setUsername(adminUsername);
            superAdmin.setPassword(passwordEncoder.encode(adminPassword));
            superAdmin.setEmail(adminEmail);
            superAdmin.setFirstName(adminFirstName);
            superAdmin.setLastName(adminLastName);
            superAdmin.setRole(Role.SUPERADMIN);
            
            userRepository.save(superAdmin);
            
            System.out.println("SUPERADMIN user created successfully!");
        } else {
            System.out.println("SUPERADMIN user already exists, skipping initialization.");
        }
    }
}
