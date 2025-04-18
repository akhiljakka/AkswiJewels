package com.akswi.akswi.controller;

import com.akswi.akswi.entity.User;
import com.akswi.akswi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin()
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public User getProfile(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public User updateProfile(@PathVariable Long userId, @RequestBody User updatedUser) {
        return userService.updateProfile(userId, updatedUser);
    }
}
