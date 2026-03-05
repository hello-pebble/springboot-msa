package com.pebble.user.adapter.in.web.dto;

import com.pebble.user.domain.User;

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
