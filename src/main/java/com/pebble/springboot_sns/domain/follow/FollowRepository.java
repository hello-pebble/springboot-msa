package com.pebble.springboot_sns.domain.follow;

import com.pebble.springboot_sns.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowingAndDeletedAtIsNull(User follower, User following);

    Optional<Follow> findByFollowerAndFollowingAndDeletedAtIsNull(User follower, User following);

    Optional<Follow> findByFollowerAndFollowingAndDeletedAtIsNotNull(User follower, User following);

    List<Follow> findByFollowingAndDeletedAtIsNull(User following);

    List<Follow> findByFollowerAndDeletedAtIsNull(User follower);
}
