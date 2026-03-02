package com.pebble.springboot_sns.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndPostIdAndDeletedAtIsNull(Long userId, Long postId);

    boolean existsByUserIdAndPostIdAndDeletedAtIsNull(Long userId, Long postId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    @Query("SELECT l.postId FROM Like l WHERE l.userId = :userId AND l.postId IN :postIds AND l.deletedAt IS NULL")
    Set<Long> findPostIdsByUserIdAndPostIdIn(Long userId, List<Long> postIds);

    @Query("SELECT l.postId FROM Like l WHERE l.userId = :userId AND l.deletedAt IS NULL ORDER BY l.createdAt DESC")
    List<Long> findLikedPostIdsByUserIdOrderByCreatedAtDesc(Long userId);
}
