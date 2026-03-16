package com.pebble.user.adapter.out.persistence;

import com.pebble.user.application.port.out.LoadProfilePort;
import com.pebble.user.application.port.out.SaveProfilePort;
import com.pebble.user.common.PersistenceAdapter;
import com.pebble.user.domain.Profile;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements LoadProfilePort, SaveProfilePort {
    private final ProfileRepository profileRepository;

    @Override
    public Optional<Profile> findByUserId(Long userId) {
        return profileRepository.findByUserIdAndDeletedAtIsNull(userId)
                .map(ProfileMapper::toDomain);
    }

    @Override
    public Profile save(Profile profile) {
        ProfileEntity entity = ProfileMapper.toEntity(profile);
        ProfileEntity savedEntity = profileRepository.save(entity);
        return ProfileMapper.toDomain(savedEntity);
    }
}
