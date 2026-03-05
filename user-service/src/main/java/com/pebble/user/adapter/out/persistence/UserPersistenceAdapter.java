package com.pebble.user.adapter.out.persistence;

import com.pebble.user.application.port.out.LoadUserPort;
import com.pebble.user.application.port.out.SaveUserPort;
import com.pebble.user.common.PersistenceAdapter;
import com.pebble.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndDeletedAtIsNull(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameAndDeletedAtIsNull(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
