package springboot_sns.domain.profile;

import com.pebble.springboot_sns.domain.profile.Profile;
import com.pebble.springboot_sns.domain.profile.ProfileException;
import com.pebble.springboot_sns.domain.profile.ProfileRepository;
import com.pebble.springboot_sns.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public void initialize(User user) {
        profileRepository.save(new Profile(user));
    }

    public Profile getByUser(User user) {
        return profileRepository.findByUserAndDeletedAtIsNull(user)
                .orElseThrow(() -> new ProfileException("프로필을 찾을 수 없습니다."));
    }

    @Transactional
    public Profile update(User user, String displayName, String bio, Long profileImageId, Long headerImageId) {
        Profile profile = profileRepository.findByUserAndDeletedAtIsNull(user)
                .orElseThrow(() -> new ProfileException("프로필을 찾을 수 없습니다."));
        profile.update(displayName, bio, profileImageId, headerImageId);
        return profile;
    }

    @Transactional
    public void updateProfileImage(User user, Long mediaId) {
        Profile profile = profileRepository.findByUserAndDeletedAtIsNull(user)
                .orElseThrow(() -> new ProfileException("프로필을 찾을 수 없습니다."));
        profile.updateProfileImage(mediaId);
    }
}
