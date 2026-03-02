package com.pebble.springboot_sns.domain.repost;

import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RepostRepository extends JpaRepository<Post, Long> {

    List<Post> findByRepostIdAndDeletedAtIsNull(Long repostId);

    boolean existsByUserAndRepostIdAndDeletedAtIsNull(User user, Long repostId);

    Optional<Post> findByUserAndIdAndDeletedAtIsNull(User user, Long id);

    @Query("SELECT p.repostId FROM Post p WHERE p.user.id = :userId AND p.repostId IN :postIds AND p.deletedAt IS NULL")
    Set<Long> findRepostIdsByUserIdAndRepostIdIn(Long userId, List<Long> postIds);
}
