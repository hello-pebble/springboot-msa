package com.pebble.auth.client.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindOrCreateUserRequest {
    private String provider;
    private String providerId;
    private String email;
    private String displayName;
    private String profileImageUrl;
}
