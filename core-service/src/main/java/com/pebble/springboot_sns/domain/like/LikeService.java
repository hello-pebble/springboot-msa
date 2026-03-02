package com.pebble.springboot_sns.domain.like;

import com.pebble.springboot_sns.domain.post.PostRepository;
import com.pebble.springboot_sns.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Like create(Long userId, Long postId) {
        userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new LikeException("사용자를 찾을 수 없습니다."));

        postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new LikeException("게시글을 찾을 수 없습니다."));

        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(userId, postId);

        if (existingLike.isPresent()) {
            Like like = existingLike.get();
            if (!like.isDeleted()) {
                throw new LikeException("이미 좋아요한 게시글입니다.");
            }
            like.restore();
            postRepository.incrementLikeCount(postId);
            return like;
        }

        Like like = new Like(userId, postId);
        likeRepository.save(like);

        postRepository.incrementLikeCount(postId);

        return like;
    }

    @Transactional
    public void delete(Long userId, Long postId) {
        userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new LikeException("사용자를 찾을 수 없습니다."));

        Like like = likeRepository.findByUserIdAndPostIdAndDeletedAtIsNull(userId, postId)
                .orElseThrow(() -> new LikeException("좋아요를 찾을 수 없습니다."));

        like.delete();

        postRepository.decrementLikeCount(postId);
    }

    public boolean isLiked(Long userId, Long postId) {
        userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new LikeException("사용자를 찾을 수 없습니다."));

        return likeRepository.existsByUserIdAndPostIdAndDeletedAtIsNull(userId, postId);
    }

    public Set<Long> findLikedPostIds(Long userId, List<Long> postIds) {
        if (postIds.isEmpty()) {
            return Set.of();
        }
        return likeRepository.findPostIdsByUserIdAndPostIdIn(userId, postIds);
    }

    public List<Long> findLikedPostIdsByUser(Long userId) {
        return likeRepository.findLikedPostIdsByUserIdOrderByCreatedAtDesc(userId);
    }
}
