package com.pebble.user.application.port.out;

import com.pebble.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    List<User> findAll();
    boolean existsByUsername(String username);
}
