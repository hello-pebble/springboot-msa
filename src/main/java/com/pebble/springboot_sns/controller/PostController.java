package com.pebble.springboot_sns.controller;

import com.pebble.springboot_sns.controller.assembler.PostResponseAssembler;
import com.pebble.springboot_sns.controller.dto.PostCreateRequest;
import com.pebble.springboot_sns.controller.dto.PostResponse;
import com.pebble.springboot_sns.domain.like.LikeService;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.post.PostViewService;
import com.pebble.springboot_sns.domain.repost.RepostService;
import com.pebble.springboot_sns.domain.timeline.TimelineService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostViewService postViewService;
    private final UserService userService;
    private final LikeService likeService;
    private final RepostService repostService;
    private final TimelineService timelineService;
    private final PostResponseAssembler postResponseAssembler;

    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostCreateRequest request,
                                               Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Post post = postService.create(request.content(), currentUser.getId(), request.mediaIds());
        timelineService.fanOut(post, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostResponse.from(post, false));
    }

    @GetMapping("/api/v1/posts")
    public ResponseEntity<List<PostResponse>> findAll(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Post> posts = postService.findAll();

        Map<Long, Post> originalPostMap = postResponseAssembler.getOriginalPostMap(posts);

        List<Long> checkIds = posts.stream()
                .map(Post::getId)
                .distinct().toList();
        Set<Long> likedPostIds = likeService.findLikedPostIds(currentUser.getId(), checkIds);
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), checkIds);

        List<PostResponse> responses = posts.stream()
                .map(post -> postResponseAssembler.toPostResponse(post, originalPostMap, likedPostIds, repostedPostIds))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/v1/posts/{postId}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId,
                                                 Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Post post = postService.findById(postId);

        if (post.isRepost()) {
            Post original = postService.findById(post.getRepostId());
            return ResponseEntity.ok(PostResponse.fromRepost(post, original,
                    likeService.isLiked(currentUser.getId(), postId),
                    repostService.isReposted(currentUser.getId(), postId)));
        }
        if (post.isQuote()) {
            Post original = postService.findById(post.getQuoteId());
            return ResponseEntity.ok(PostResponse.fromQuote(post, original,
                    likeService.isLiked(currentUser.getId(), postId),
                    repostService.isReposted(currentUser.getId(), postId)));
        }
        return ResponseEntity.ok(PostResponse.from(post,
                likeService.isLiked(currentUser.getId(), postId),
                repostService.isReposted(currentUser.getId(), postId)));
    }

    @PostMapping("/api/v1/posts/{postId}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long postId) {
        postService.findById(postId);
        postViewService.incrementViewCount(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/users/{userId}/posts")
    public ResponseEntity<List<PostResponse>> findByUser(@PathVariable Long userId,
                                                         Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Post> posts = postService.findByUser(userId);

        Map<Long, Post> originalPostMap = postResponseAssembler.getOriginalPostMap(posts);

        List<Long> checkIds = posts.stream()
                .map(Post::getId)
                .distinct().toList();
        Set<Long> likedPostIds = likeService.findLikedPostIds(currentUser.getId(), checkIds);
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), checkIds);

        List<PostResponse> responses = posts.stream()
                .map(post -> postResponseAssembler.toPostResponse(post, originalPostMap, likedPostIds, repostedPostIds))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/api/v1/posts/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        postService.delete(postId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
