package com.pebble.user.adapter.out.persistence;

import com.pebble.user.domain.Profile;

public class ProfileMapper {
    public static Profile toDomain(ProfileEntity entity) {
        if (entity == null) return null;
        return Profile.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .displayName(entity.getDisplayName())
                .bio(entity.getBio())
                .profileImageId(entity.getProfileImageId())
                .headerImageId(entity.getHeaderImageId())
                .build();
    }

    public static ProfileEntity toEntity(Profile domain) {
        if (domain == null) return null;
        return ProfileEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .displayName(domain.getDisplayName())
                .bio(domain.getBio())
                .profileImageId(domain.getProfileImageId())
                .headerImageId(domain.getHeaderImageId())
                .build();
    }
}
