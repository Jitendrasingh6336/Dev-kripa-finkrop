package com.example.dev.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.dev.model.Role;
import com.example.dev.model.User;
import com.example.dev.model.UserStatus;
import com.example.dev.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class StartUpDataLoader implements CommandLineRunner {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
    public void run(String... args) throws Exception {

        // Check if ADMIN already exists
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (!adminExists) {

            User admin = User.builder()
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("Admin@123")) 
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .isActive(true)
                    .isDeleted(false)
                    .build();

            userRepository.save(admin);

            System.out.println("✅ Default Admin Created Successfully!");
        } else {
            System.out.println("ℹ️ Admin already exists.");
        }
    }
	
}
