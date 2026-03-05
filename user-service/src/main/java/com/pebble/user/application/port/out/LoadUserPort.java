package com.pebble.user.application.port.out;

import com.pebble.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> findAll();
    boolean existsByUsername(String username);
}
