package com.pebble.user.application.service;

import com.pebble.user.application.port.in.ProfileUseCase;
import com.pebble.user.application.port.out.LoadProfilePort;
import com.pebble.user.application.port.out.SaveProfilePort;
import com.pebble.user.application.port.out.MediaPort;
import com.pebble.user.adapter.in.web.dto.MediaInitResponse;
import com.pebble.user.common.UseCase;
import com.pebble.user.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService implements ProfileUseCase {
    private final LoadProfilePort loadProfilePort;
    private final SaveProfilePort saveProfilePort;
    private final MediaPort mediaPort;

    @Override
    @Transactional
    public void initialize(Long userId, String displayName) {
        saveProfilePort.save(Profile.initialize(userId, displayName));
    }

    @Override
    public Profile getByUserId(Long userId) {
        return loadProfilePort.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Profile update(Long userId, String displayName, String bio, Long profileImageId, Long headerImageId) {
        Profile profile = getByUserId(userId);
        profile.update(displayName, bio, profileImageId, headerImageId);
        return saveProfilePort.save(profile);
    }

    @Override
    @Transactional
    public void updateProfileImage(Long userId, Long mediaId) {
        Profile profile = getByUserId(userId);
        profile.updateProfileImage(mediaId);
        saveProfilePort.save(profile);
        mediaPort.completeUpload(userId, mediaId);
    }

    @Override
    public MediaInitResponse initProfileImageUpload(Long userId, Long fileSize, String fileName) {
        return mediaPort.initUpload(userId, "IMAGE", fileSize, fileName);
    }
}
