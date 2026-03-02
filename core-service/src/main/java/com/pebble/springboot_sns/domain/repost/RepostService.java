package com.pebble.springboot_sns.domain.repost;

import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostRepository;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RepostService {

    private final RepostRepository repostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post create(Long userId, Long repostId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RepostException("사용자를 찾을 수 없습니다."));

        if (repostRepository.existsByUserAndRepostIdAndDeletedAtIsNull(user, repostId)) {
            throw new RepostException("이미 리포스트한 게시글입니다.");
        }

        if (!postRepository.existsByIdAndDeletedAtIsNull(repostId)) {
            throw new RepostException("리포스트할 게시글을 찾을 수 없습니다.");
        }

        postRepository.incrementRepostCount(repostId);

        Post repost = Post.createRepost(user, repostId);
        return repostRepository.save(repost);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RepostException("사용자를 찾을 수 없습니다."));

        Post repost = repostRepository.findByUserAndIdAndDeletedAtIsNull(user, id)
                .orElseThrow(() -> new RepostException("리포스트를 찾을 수 없습니다."));

        postRepository.decrementRepostCount(repost.getRepostId());
        repost.delete();
    }

    public List<Post> findByRepostId(Long repostId) {
        postRepository.findByIdAndDeletedAtIsNull(repostId)
                .orElseThrow(() -> new RepostException("게시글을 찾을 수 없습니다."));

        return repostRepository.findByRepostIdAndDeletedAtIsNull(repostId);
    }

    public boolean isReposted(Long userId, Long postId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RepostException("사용자를 찾을 수 없습니다."));

        return repostRepository.existsByUserAndRepostIdAndDeletedAtIsNull(user, postId);
    }

    public Set<Long> findRepostedPostIds(Long userId, List<Long> postIds) {
        if (postIds.isEmpty()) {
            return Set.of();
        }
        return repostRepository.findRepostIdsByUserIdAndRepostIdIn(userId, postIds);
    }
}
