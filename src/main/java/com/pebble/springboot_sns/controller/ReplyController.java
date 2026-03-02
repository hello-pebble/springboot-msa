package com.pebble.springboot_sns.controller;

import com.pebble.springboot_sns.controller.dto.PostCreateRequest;
import com.pebble.springboot_sns.controller.dto.PostResponse;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.like.LikeService;
import com.pebble.springboot_sns.domain.reply.ReplyService;
import com.pebble.springboot_sns.domain.repost.RepostService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
    private final UserService userService;
    private final LikeService likeService;
    private final RepostService repostService;

    @PostMapping("/api/v1/posts/{postId}/replies")
    public ResponseEntity<PostResponse> createReply(@PathVariable Long postId,
                                                    @Valid @RequestBody PostCreateRequest request,
                                                    Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Post reply = replyService.create(request.content(), currentUser.getId(), postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(reply));
    }

    @GetMapping("/api/v1/posts/{postId}/replies")
    public ResponseEntity<List<PostResponse>> findReplies(@PathVariable Long postId,
                                                          Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Post> replies = replyService.findByParentId(postId);
        List<Long> replyIds = replies.stream().map(Post::getId).toList();
        Set<Long> likedPostIds = likeService.findLikedPostIds(currentUser.getId(), replyIds);
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), replyIds);
        List<PostResponse> responses = replies.stream()
                .map(reply -> PostResponse.from(reply,
                        likedPostIds.contains(reply.getId()),
                        repostedPostIds.contains(reply.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/api/v1/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId,
                                            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        replyService.delete(replyId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
