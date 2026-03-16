package com.pebble.user.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindOrCreateUserRequest {
    @NotBlank
    private String provider;
    @NotBlank
    private String providerId;
    @Email
    @NotBlank
    private String email;
    private String displayName;
    private String profileImageUrl;
}
