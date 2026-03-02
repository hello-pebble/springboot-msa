package springboot_sns.controller;

import com.pebble.springboot_sns.controller.assembler.PostResponseAssembler;
import com.pebble.springboot_sns.controller.dto.PostResponse;
import com.pebble.springboot_sns.controller.dto.TimelineResponse;
import com.pebble.springboot_sns.domain.like.LikeService;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.repost.RepostService;
import com.pebble.springboot_sns.domain.timeline.TimelineService;
import com.pebble.springboot_sns.domain.user.User;
import com.pebble.springboot_sns.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineService timelineService;
    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;
    private final RepostService repostService;
    private final PostResponseAssembler postResponseAssembler;

    @GetMapping("/api/v1/timelines")
    public ResponseEntity<TimelineResponse> getTimeline(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int limit,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());

        TimelineService.TimelineResult result = timelineService.getTimeline(currentUser.getId(), cursor, limit);

        List<Post> posts = postService.findAllByIds(result.postIds());

        Map<Long, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, Function.identity()));
        List<Post> orderedPosts = result.postIds().stream()
                .filter(postMap::containsKey)
                .map(postMap::get)
                .toList();

        Map<Long, Post> originalPostMap = postResponseAssembler.getOriginalPostMap(orderedPosts);
        List<Long> checkIds = orderedPosts.stream().map(Post::getId).distinct().toList();
        Set<Long> likedPostIds = likeService.findLikedPostIds(currentUser.getId(), checkIds);
        Set<Long> repostedPostIds = repostService.findRepostedPostIds(currentUser.getId(), checkIds);

        List<PostResponse> postResponses = orderedPosts.stream()
                .map(post -> postResponseAssembler.toPostResponse(post, originalPostMap, likedPostIds, repostedPostIds))
                .toList();

        return ResponseEntity.ok(new TimelineResponse(postResponses, result.nextCursor()));
    }
}
