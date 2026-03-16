package com.pebble.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Profile {
    private Long id;
    private Long userId;
    private String displayName;
    private String bio;
    private Long profileImageId;
    private Long headerImageId;

    public static Profile initialize(Long userId, String displayName) {
        return Profile.builder()
                .userId(userId)
                .displayName(displayName)
                .build();
    }

    public void update(String displayName, String bio, Long profileImageId, Long headerImageId) {
        this.displayName = displayName;
        this.bio = bio;
        this.profileImageId = profileImageId;
        this.headerImageId = headerImageId;
    }

    public void updateProfileImage(Long profileImageId) {
        this.profileImageId = profileImageId;
    }
}
