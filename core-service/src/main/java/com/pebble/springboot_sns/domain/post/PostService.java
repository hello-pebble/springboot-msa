package com.pebble.springboot_sns.domain.post;

import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post create(String content, Long userId, List<Long> mediaIds) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new PostException("사용자를 찾을 수 없습니다."));

        Post post = new Post(content, user, mediaIds);
        return postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new PostException("게시글을 찾을 수 없습니다."));
    }

    public List<Post> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return postRepository.findAllByIdInAndDeletedAtIsNull(ids);
    }

    public List<Post> findAll() {
        return postRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
    }

    public List<Post> findByUser(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new PostException("사용자를 찾을 수 없습니다."));

        return postRepository.findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(user);
    }

    public List<Post> findByUserExcludingReplies(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new PostException("사용자를 찾을 수 없습니다."));

        return postRepository.findByUserAndParentIdIsNullAndDeletedAtIsNullOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void incrementReplyCount(Long postId) {
        postRepository.incrementReplyCount(postId);
    }

    @Transactional
    public void decrementReplyCount(Long postId) {
        postRepository.decrementReplyCount(postId);
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        Post post = findById(postId);

        if (!post.getUser().getId().equals(userId)) {
            throw new PostException("본인의 게시글만 삭제할 수 있습니다.");
        }

        post.delete();
    }
}
