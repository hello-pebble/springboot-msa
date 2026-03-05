package com.pebble.user.adapter.in.web.dto;

public record LoginRequest(
        String username,
        String password
) {
}
