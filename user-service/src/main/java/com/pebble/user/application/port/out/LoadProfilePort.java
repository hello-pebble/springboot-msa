package com.pebble.user.application.port.out;

import com.pebble.user.domain.Profile;
import java.util.Optional;

public interface LoadProfilePort {
    Optional<Profile> findByUserId(Long userId);
}
