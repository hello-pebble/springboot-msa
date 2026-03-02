package com.pebble.springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.user.User;

public record FollowResponse(
        Long id,
        String username
) {
    public static FollowResponse from(User user) {
        return new FollowResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
