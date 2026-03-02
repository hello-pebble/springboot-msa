package com.pebble.springboot_sns.controller.dto;

public record LoginRequest(
        String username,
        String password
) {
}
