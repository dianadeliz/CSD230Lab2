package com.lab2.discussion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminPassword = "admin123"; // Change this to a strong password for production!
        String adminRole = "ADMIN";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(adminRole);
            userRepository.save(admin);
            System.out.println("Admin user created: username='admin', password='admin123'");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
} 