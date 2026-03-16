package com.pebble.user.application.port.out;

import com.pebble.user.domain.Profile;

public interface SaveProfilePort {
    Profile save(Profile profile);
}
