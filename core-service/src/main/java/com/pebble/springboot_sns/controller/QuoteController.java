package com.pebble.springboot_sns.controller;

import com.pebble.springboot_sns.controller.dto.PostCreateRequest;
import com.pebble.springboot_sns.controller.dto.PostResponse;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.quote.QuoteService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/api/v1/posts/{postId}/quotes")
    public ResponseEntity<PostResponse> createQuote(@PathVariable Long postId,
                                                    @Valid @RequestBody PostCreateRequest request,
                                                    Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Post quote = quoteService.create(request.content(), currentUser.getId(), postId);
        Post original = postService.findById(postId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostResponse.fromQuote(quote, original, false, false));
    }

    @GetMapping("/api/v1/posts/{postId}/quotes")
    public ResponseEntity<List<PostResponse>> findQuotes(@PathVariable Long postId) {
        Post original = postService.findById(postId);
        List<PostResponse> responses = quoteService.findByQuoteId(postId).stream()
                .map(quote -> PostResponse.fromQuote(quote, original, false, false))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/api/v1/quotes/{quoteId}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long quoteId,
                                            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        quoteService.delete(quoteId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
