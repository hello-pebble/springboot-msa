package springboot_sns.controller;

import com.pebble.springboot_sns.controller.dto.FollowCountResponse;
import com.pebble.springboot_sns.controller.dto.FollowResponse;
import com.pebble.springboot_sns.domain.follow.FollowCountService;
import com.pebble.springboot_sns.domain.follow.FollowService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final FollowCountService followCountService;
    private final UserService userService;

    @PostMapping("/api/v1/follows/{targetUserId}")
    public ResponseEntity<Void> follow(@PathVariable Long targetUserId, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        followService.follow(currentUser.getId(), targetUserId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/api/v1/follows/{targetUserId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long targetUserId, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        followService.unfollow(currentUser.getId(), targetUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/v1/follows/{userId}/followers")
    public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable Long userId) {
        List<FollowResponse> responses = followService.getFollowers(userId).stream()
                .map(FollowResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/v1/follows/{userId}/following")
    public ResponseEntity<List<FollowResponse>> getFollowing(@PathVariable Long userId) {
        List<FollowResponse> responses = followService.getFollowings(userId).stream()
                .map(FollowResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/v1/follows/{userId}/follow_count")
    public ResponseEntity<FollowCountResponse> getFollowCount(@PathVariable Long userId) {
        User user = userService.findById(userId);
        FollowCountResponse response = FollowCountResponse.from(followCountService.getByUser(user));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/follows/check/{targetUserId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long targetUserId, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        boolean following = followService.isFollowing(currentUser.getId(), targetUserId);
        return ResponseEntity.ok(following);
    }
}
