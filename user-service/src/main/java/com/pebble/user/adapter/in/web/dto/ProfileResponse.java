package com.pebble.user.adapter.in.web.dto;

import com.pebble.user.domain.Profile;
import com.pebble.user.domain.User;

public record ProfileResponse(
        Long userId,
        String username,
        String displayName,
        String bio,
        Long profileImageId,
        Long headerImageId
) {
    public static ProfileResponse from(Profile profile, User user) {
        return new ProfileResponse(
                user.getId(),
                user.getUsername(),
                profile.getDisplayName(),
                profile.getBio(),
                profile.getProfileImageId(),
                profile.getHeaderImageId()
        );
    }
}
