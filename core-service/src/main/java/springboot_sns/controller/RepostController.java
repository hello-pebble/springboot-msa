package springboot_sns.controller;

import com.pebble.springboot_sns.controller.dto.PostResponse;
import com.pebble.springboot_sns.domain.like.LikeService;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.repost.RepostService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class RepostController {

    private final RepostService repostService;
    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;

    @PostMapping("/api/v1/reposts/{postId}")
    public ResponseEntity<PostResponse> createRepost(@PathVariable Long postId,
                                                     Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Post repost = repostService.create(currentUser.getId(), postId);
        Post original = postService.findById(postId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostResponse.fromRepost(repost, original, false, true));
    }

    @DeleteMapping("/api/v1/reposts/{postId}")
    public ResponseEntity<Void> deleteRepost(@PathVariable Long postId,
                                             Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        repostService.delete(currentUser.getId(), postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/reposts/{postId}")
    public ResponseEntity<List<PostResponse>> findReposts(@PathVariable Long postId,
                                                          Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        Post original = postService.findById(postId);
        List<Post> reposts = repostService.findByRepostId(postId);
        List<Long> repostIds = reposts.stream().map(Post::getId).toList();
        Set<Long> likedPostIds = likeService.findLikedPostIds(currentUser.getId(), repostIds);
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), repostIds);
        List<PostResponse> responses = reposts.stream()
                .map(repost -> PostResponse.fromRepost(repost, original,
                        likedPostIds.contains(repost.getId()),
                        repostedPostIds.contains(repost.getId())))
                .toList();
        return ResponseEntity.ok(responses);
    }
}
