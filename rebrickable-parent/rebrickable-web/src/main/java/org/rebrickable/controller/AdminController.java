package org.rebrickable.controller;

import org.rebrickable.User;
import org.rebrickable.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) { 
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/promote/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User promoteToAdmin(@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole("ADMIN");
        return userRepository.save(user);
    } 
    
}
