package com.pebble.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User {
    private Long id;
    private String username; // This will likely store the email
    private String password; // Can be null for OAuth2 users
    private String provider; // e.g., "local", "google"
    private String providerId; // Unique ID from the provider
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.provider = "local";
    }

    public User(String username, String provider, String providerId) {
        this.username = username;
        this.provider = provider;
        this.providerId = providerId;
    }

    // 비지니스 규칙
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
