package com.pebble.springboot_sns.domain.user;

import com.pebble.springboot_sns.domain.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;

    @Transactional
    public User signUp(String username, String password) {

        if (userRepository.existsByUsernameAndDeletedAtIsNull(username)) {
            throw new UserException("이미 존재하는 사용자명입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = userRepository.save(new User(username, encodedPassword));
        profileService.initialize(user);
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
    }

    public User findById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
