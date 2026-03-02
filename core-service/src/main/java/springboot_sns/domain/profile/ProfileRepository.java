package springboot_sns.domain.profile;

import com.pebble.springboot_sns.domain.profile.Profile;
import com.pebble.springboot_sns.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUserAndDeletedAtIsNull(User user);
}
