package com.pebble.springboot_sns.domain.follow;

import com.pebble.springboot_sns.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowCountService {

    private final FollowCountRepository followCountRepository;

    public FollowCount initialize(User user) {
        return followCountRepository.save(new FollowCount(user));
    }

    public FollowCount getByUser(User user) {
        return followCountRepository.findByUserAndDeletedAtIsNull(user)
                .orElseGet(() -> new FollowCount(user));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initializeIfNotExists(User user) {
        if (followCountRepository.findByUserAndDeletedAtIsNull(user).isEmpty()) {
            try {
                followCountRepository.save(new FollowCount(user));
            } catch (DataIntegrityViolationException ignored) {
                // 동시 요청으로 다른 트랜잭션이 먼저 생성한 경우 무시
            }
        }
    }

    @Transactional
    public void incrementFollowerCount(Long userId) {
        followCountRepository.incrementFollowerCount(userId);
    }

    @Transactional
    public void decrementFollowerCount(Long userId) {
        followCountRepository.decrementFollowerCount(userId);
    }

    @Transactional
    public void incrementFollowingCount(Long userId) {
        followCountRepository.incrementFollowingCount(userId);
    }

    @Transactional
    public void decrementFollowingCount(Long userId) {
        followCountRepository.decrementFollowingCount(userId);
    }
}
