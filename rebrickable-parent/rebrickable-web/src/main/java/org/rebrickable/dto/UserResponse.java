package org.rebrickable.dto;

import org.rebrickable.User;

public record UserResponse(String id, String username, String email, String role) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
