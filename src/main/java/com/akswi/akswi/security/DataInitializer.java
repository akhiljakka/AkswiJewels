package com.akswi.akswi.security;


import com.akswi.akswi.entity.User;
import com.akswi.akswi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner loadDefaultUser() {
        return args -> {
            // Only create a default user if none exist
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("akhil.jakka@gmail.com");
                // Ensure you encode the password with the same encoder used during authentication
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("admin"); // In your CustomUserDetailsService, ROLE_ prefix is added automatically.
                userRepository.save(admin);
                System.out.println("Default admin user created with username 'admin' and password 'admin'");
            }
        };
    }
}

