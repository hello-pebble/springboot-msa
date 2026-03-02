package com.pebble.springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.follow.FollowCount;
import com.pebble.springboot_sns.domain.profile.Profile;

public record ProfileResponse(
        Long userId,
        String username,
        String displayName,
        String bio,
        Long profileImageId,
        Long headerImageId,
        Long followerCount,
        Long followingCount
) {
    public static ProfileResponse from(Profile profile, FollowCount followCount) {
        return new ProfileResponse(
                profile.getUser().getId(),
                profile.getUser().getUsername(),
                profile.getDisplayName(),
                profile.getBio(),
                profile.getProfileImageId(),
                profile.getHeaderImageId(),
                followCount.getFollowerCount(),
                followCount.getFollowingCount()
        );
    }
}
