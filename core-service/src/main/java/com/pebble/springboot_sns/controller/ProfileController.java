package com.pebble.springboot_sns.controller;

import com.pebble.springboot_sns.controller.assembler.PostResponseAssembler;
import com.pebble.springboot_sns.controller.dto.*;
import com.pebble.springboot_sns.domain.follow.FollowCount;
import com.pebble.springboot_sns.domain.follow.FollowCountService;
import com.pebble.springboot_sns.domain.like.LikeService;
import com.pebble.springboot_sns.domain.media.MediaService;
import com.pebble.springboot_sns.domain.media.MediaType;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.profile.Profile;
import com.pebble.springboot_sns.domain.profile.ProfileService;
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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final FollowCountService followCountService;
    private final UserService userService;
    private final PostService postService;
    private final ReplyService replyService;
    private final LikeService likeService;
    private final RepostService repostService;
    private final MediaService mediaService;
    private final PostResponseAssembler postResponseAssembler;

    @GetMapping("/api/v1/users/{userId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        User user = userService.findById(userId);
        Profile profile = profileService.getByUser(user);
        FollowCount followCount = followCountService.getByUser(user);
        return ResponseEntity.ok(ProfileResponse.from(profile, followCount));
    }

    @PutMapping("/api/v1/users/me/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody @Valid ProfileUpdateRequest request,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Profile profile = profileService.update(
                currentUser,
                request.displayName(),
                request.bio(),
                request.profileImageId(),
                request.headerImageId()
        );
        FollowCount followCount = followCountService.getByUser(currentUser);
        return ResponseEntity.ok(ProfileResponse.from(profile, followCount));
    }

    @GetMapping("/api/v1/users/me/posts")
    public ResponseEntity<List<PostResponse>> getMyPosts(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Post> posts = postService.findByUserExcludingReplies(currentUser.getId());

        Map<Long, Post> originalPostMap = postResponseAssembler.getOriginalPostMap(posts);
        List<Long> postIds = posts.stream().map(Post::getId).distinct().toList();
        Set<Long> likedPostIds = likeService.findLikedPostIds(currentUser.getId(), postIds);
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), postIds);

        List<PostResponse> responses = posts.stream()
                .map(post -> postResponseAssembler.toPostResponse(post, originalPostMap, likedPostIds, repostedPostIds))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/v1/users/me/replies")
    public ResponseEntity<List<PostResponse>> getMyReplies(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Post> replies = replyService.findByUser(currentUser);

        List<Long> replyIds = replies.stream().map(Post::getId).distinct().toList();
        Set<Long> likedPostIds = likeService.findLikedPostIds(currentUser.getId(), replyIds);
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), replyIds);

        List<PostResponse> responses = replies.stream()
                .map(reply -> PostResponse.from(reply,
                        likedPostIds.contains(reply.getId()),
                        repostedPostIds.contains(reply.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/v1/users/me/likes")
    public ResponseEntity<List<PostResponse>> getMyLikes(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        List<Long> likedPostIds = likeService.findLikedPostIdsByUser(currentUser.getId());
        List<Post> posts = postService.findAllByIds(likedPostIds);

        Map<Long, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, Function.identity()));
        List<Post> orderedPosts = likedPostIds.stream()
                .filter(postMap::containsKey)
                .map(postMap::get)
                .toList();

        Map<Long, Post> originalPostMap = postResponseAssembler.getOriginalPostMap(orderedPosts);
        List<Long> postIds = orderedPosts.stream().map(Post::getId).distinct().toList();
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), postIds);

        List<PostResponse> responses = orderedPosts.stream()
                .map(post -> postResponseAssembler.toPostResponse(post, originalPostMap, Set.copyOf(likedPostIds), repostedPostIds))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/api/v1/users/me/profile/image/init")
    public ResponseEntity<MediaInitResponse> initProfileImage(
            @RequestBody @Valid ProfileImageInitRequest request,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        MediaService.UploadInitResult result = mediaService.initUpload(
                currentUser.getId(), MediaType.IMAGE, request.fileSize(), request.fileName());
        return ResponseEntity.status(HttpStatus.CREATED).body(MediaInitResponse.from(result));
    }

    @PostMapping("/api/v1/users/me/profile/image/uploaded")
    public ResponseEntity<Void> uploadedProfileImage(
            @RequestBody @Valid ProfileImageUploadedRequest request,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        mediaService.completeUpload(currentUser.getId(), request.mediaId(), null);
        profileService.updateProfileImage(currentUser, request.mediaId());
        return ResponseEntity.ok().build();
    }
}
