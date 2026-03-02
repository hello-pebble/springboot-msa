package com.pebble.springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.user.User;

public record UserResponse(
        Long id,
        String username
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
