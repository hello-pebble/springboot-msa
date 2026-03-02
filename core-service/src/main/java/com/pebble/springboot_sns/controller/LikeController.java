package com.pebble.springboot_sns.controller;

import com.pebble.springboot_sns.controller.dto.LikeResponse;
import com.pebble.springboot_sns.controller.dto.LikeStatusResponse;
import com.pebble.springboot_sns.domain.like.Like;
import com.pebble.springboot_sns.domain.like.LikeService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("/api/v1/likes/{postId}")
    public ResponseEntity<LikeResponse> createLike(@PathVariable Long postId,
                                                   Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Like like = likeService.create(currentUser.getId(), postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(LikeResponse.from(like));
    }

    @DeleteMapping("/api/v1/likes/{postId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long postId,
                                           Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        likeService.delete(currentUser.getId(), postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/likes/{postId}/me")
    public ResponseEntity<LikeStatusResponse> isLiked(@PathVariable Long postId,
                                                      Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        boolean isLikedByMe = likeService.isLiked(currentUser.getId(), postId);
        return ResponseEntity.ok(new LikeStatusResponse(isLikedByMe));
    }
}
