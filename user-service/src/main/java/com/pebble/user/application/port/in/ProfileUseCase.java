package com.pebble.user.application.port.in;

import com.pebble.user.adapter.in.web.dto.MediaInitResponse;
import com.pebble.user.domain.Profile;

public interface ProfileUseCase {
    void initialize(Long userId, String displayName);
    Profile getByUserId(Long userId);
    Profile update(Long userId, String displayName, String bio, Long profileImageId, Long headerImageId);
    void updateProfileImage(Long userId, Long mediaId);
    MediaInitResponse initProfileImageUpload(Long userId, Long fileSize, String fileName);
}
