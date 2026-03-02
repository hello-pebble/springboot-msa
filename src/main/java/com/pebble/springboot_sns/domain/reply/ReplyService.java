package com.pebble.springboot_sns.domain.reply;

import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public Post create(String content, Long userId, Long parentId) {
        User user = userService.findById(userId);
        postService.findById(parentId);
        postService.incrementReplyCount(parentId);

        Post reply = Post.createReply(content, user, parentId);
        return replyRepository.save(reply);
    }

    public List<Post> findByUser(User user) {
        return replyRepository.findByUserAndParentIdIsNotNullAndDeletedAtIsNullOrderByCreatedAtDesc(user);
    }

    public List<Post> findByParentId(Long parentId) {
        postService.findById(parentId);
        return replyRepository.findByParentIdAndDeletedAtIsNullOrderByCreatedAtDesc(parentId);
    }

    @Transactional
    public void delete(Long replyId, Long userId) {
        Post reply = postService.findById(replyId);

        if (!reply.isReply()) {
            throw new ReplyException("답글이 아닙니다.");
        }

        if (!reply.getUser().getId().equals(userId)) {
            throw new ReplyException("본인의 답글만 삭제할 수 있습니다.");
        }

        postService.decrementReplyCount(reply.getParentId());
        reply.delete();
    }
}
