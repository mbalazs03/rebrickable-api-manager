package org.rebrickable.controller;

import org.rebrickable.User;
import org.rebrickable.repository.UserRepository;
import org.rebrickable.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AdminController(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        logger.info("Admin requesting all users list");
        List<User> users = userRepository.findAll();
        logger.debug("Retrieved {} users", users.size());
        return users;
    }

    @PostMapping("/promote/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User promoteToAdmin(@PathVariable String id) {
        logger.info("Promoting user to admin. User ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Failed to promote user: ID {} not found", id);
                    return new RuntimeException("User not found");
                });
        user.setRole("ADMIN");
        User savedUser = userRepository.save(user);
        logger.info("Successfully promoted user {} to admin role", user.getUsername());
        return savedUser;
    }

    @PostMapping("/revoke/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User revokeAdmin(@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole("USER");
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        logger.info("Attempting to delete user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Delete user failed: ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
        userRepository.deleteById(id);
        logger.info("Successfully deleted user with ID: {}", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/impersonate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> impersonateUser(@PathVariable String id) {
        logger.info("Admin attempting to impersonate user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        logger.info("Successfully generated impersonation token for user: {}", user.getUsername());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        return userRepository.save(user);
    }
}