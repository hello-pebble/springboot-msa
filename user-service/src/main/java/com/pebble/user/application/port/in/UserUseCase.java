package com.pebble.user.application.port.in;

import com.pebble.user.domain.User;

import java.util.List;

public interface UserUseCase {
    User signUp(String username, String password);
    User findByUsername(String username);
    User findById(Long id);
    List<User> findAll();
}
