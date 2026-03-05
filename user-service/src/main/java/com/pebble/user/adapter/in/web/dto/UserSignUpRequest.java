package com.pebble.user.adapter.in.web.dto;

public record UserSignUpRequest(
        String username,
        String password
) {
}
