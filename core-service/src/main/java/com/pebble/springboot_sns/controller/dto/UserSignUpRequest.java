package com.pebble.springboot_sns.controller.dto;

public record UserSignUpRequest(
        String username,
        String password
) {
}
