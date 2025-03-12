package org.rebrickable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenUserCreated_thenDefaultRoleIsUser() {
        User user = new User();

        assertEquals("USER", user.getRole());
    }

    @Test
    void whenUsernameIsBlank_thenValidationFails() {
        User user = new User();
        user.setUsername("");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().equals("Username cannot be blank")));
    }

    @Test
    void whenUsernameTooShort_thenValidationFails() {
        User user = new User();
        user.setUsername("ab");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().equals("Username must be between 3 and 50 characters")));
    }

    @Test
    void whenPasswordTooShort_thenValidationFails() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("12345");
        user.setEmail("test@example.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().equals("Password must be at least 6 characters long")));
    }

    @Test
    void whenEmailInvalid_thenValidationFails() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().equals("Email should be valid")));
    }

    @Test
    void whenAllFieldsValid_thenValidationSucceeds() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }
}