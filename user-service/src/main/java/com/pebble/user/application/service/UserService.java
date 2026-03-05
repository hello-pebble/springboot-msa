package com.pebble.user.application.service;

import com.pebble.user.application.port.in.UserUseCase;
import com.pebble.user.application.port.out.LoadUserPort;
import com.pebble.user.application.port.out.SaveUserPort;
import com.pebble.user.common.UseCase;
import com.pebble.user.domain.User;
import com.pebble.user.domain.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordEncoder passwordEncoder;
    // private final ProfileService profileService; // TODO: handle inter-service call or event

    @Override
    @Transactional
    public User signUp(String username, String password) {
        if (loadUserPort.existsByUsername(username)) {
            throw new UserException("이미 존재하는 사용자명입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);
        User savedUser = saveUserPort.save(user);

        // profileService.initialize(savedUser); // This should be handled via event or inter-service call

        return savedUser;
    }

    @Override
    public User findByUsername(String username) {
        return loadUserPort.findByUsername(username)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public User findById(Long id) {
        return loadUserPort.findById(id)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public List<User> findAll() {
        return loadUserPort.findAll();
    }
}
