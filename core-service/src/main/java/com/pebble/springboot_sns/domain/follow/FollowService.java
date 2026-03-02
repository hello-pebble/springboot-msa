package com.pebble.springboot_sns.domain.follow;

import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.timeline.TimelineRepository;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final FollowCountService followCountService;
    private final UserRepository userRepository;
    private final PostService postService;
    private final TimelineRepository timelineRepository;

    @Transactional
    public Follow follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new FollowException("자기 자신을 팔로우할 수 없습니다.");
        }

        User follower = userRepository.findByIdAndDeletedAtIsNull(followerId)
                .orElseThrow(() -> new FollowException("팔로워 사용자를 찾을 수 없습니다."));
        User following = userRepository.findByIdAndDeletedAtIsNull(followingId)
                .orElseThrow(() -> new FollowException("팔로잉 사용자를 찾을 수 없습니다."));

        if (followRepository.existsByFollowerAndFollowingAndDeletedAtIsNull(follower, following)) {
            throw new FollowException("이미 팔로우한 사용자입니다.");
        }

        followCountService.initializeIfNotExists(follower);
        followCountService.initializeIfNotExists(following);

        Follow follow = followRepository.findByFollowerAndFollowingAndDeletedAtIsNotNull(follower, following)
                .map(existing -> {
                    existing.restore();
                    return followRepository.save(existing);
                })
                .orElseGet(() -> followRepository.save(new Follow(follower, following)));

        followCountService.incrementFollowingCount(followerId);
        followCountService.incrementFollowerCount(followingId);

        return follow;
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findByIdAndDeletedAtIsNull(followerId)
                .orElseThrow(() -> new FollowException("팔로워 사용자를 찾을 수 없습니다."));
        User following = userRepository.findByIdAndDeletedAtIsNull(followingId)
                .orElseThrow(() -> new FollowException("팔로잉 사용자를 찾을 수 없습니다."));

        Follow follow = followRepository.findByFollowerAndFollowingAndDeletedAtIsNull(follower, following)
                .orElseThrow(() -> new FollowException("팔로우 관계가 존재하지 않습니다."));

        follow.delete();
        followRepository.save(follow);

        followCountService.decrementFollowingCount(followerId);
        followCountService.decrementFollowerCount(followingId);

        postService.findByUser(followingId)
                .forEach(post -> timelineRepository.remove(followerId, post.getId()));
    }

    public List<User> getFollowers(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다."));

        return followRepository.findByFollowingAndDeletedAtIsNull(user).stream()
                .map(Follow::getFollower)
                .toList();
    }

    public List<User> getFollowings(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new FollowException("사용자를 찾을 수 없습니다."));

        return followRepository.findByFollowerAndDeletedAtIsNull(user).stream()
                .map(Follow::getFollowing)
                .toList();
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = userRepository.findByIdAndDeletedAtIsNull(followerId)
                .orElseThrow(() -> new FollowException("팔로워 사용자를 찾을 수 없습니다."));
        User following = userRepository.findByIdAndDeletedAtIsNull(followingId)
                .orElseThrow(() -> new FollowException("팔로잉 사용자를 찾을 수 없습니다."));

        return followRepository.existsByFollowerAndFollowingAndDeletedAtIsNull(follower, following);
    }
}
