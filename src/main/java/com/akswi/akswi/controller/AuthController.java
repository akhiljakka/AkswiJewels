package com.akswi.akswi.controller;

import com.akswi.akswi.dto.LoginRequest;
import com.akswi.akswi.entity.User;
import com.akswi.akswi.repository.UserRepository;
import com.akswi.akswi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin()  // Allow cross-origin requests from your React app
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) {
        // Find the user by username
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        // Optionally, remove sensitive information before returning
        user.setPassword(null);
        return user;
    }
}
