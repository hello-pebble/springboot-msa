package com.pebble.user.application.port.out;

import com.pebble.user.domain.User;

public interface SaveUserPort {
    User save(User user);
}
