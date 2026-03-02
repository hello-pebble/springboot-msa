package springboot_sns.controller.assembler;

import com.pebble.springboot_sns.controller.dto.PostResponse;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostResponseAssembler {

    private final PostService postService;

    public Map<Long, Post> getOriginalPostMap(List<Post> posts) {
        List<Long> originalIds = new ArrayList<>();
        for (Post post : posts) {
            if (post.isRepost()) {
                originalIds.add(post.getRepostId());
            } else if (post.isQuote()) {
                originalIds.add(post.getQuoteId());
            }
        }
        List<Long> distinctIds = originalIds.stream().distinct().toList();
        return postService.findAllByIds(distinctIds).stream()
                .collect(Collectors.toMap(Post::getId, Function.identity()));
    }

    public PostResponse toPostResponse(Post post, Map<Long, Post> originalPostMap,
                                       Set<Long> likedPostIds, Set<Long> repostedPostIds) {
        if (post.isRepost()) {
            Post original = originalPostMap.get(post.getRepostId());
            if (original != null) {
                return PostResponse.fromRepost(post, original,
                        likedPostIds.contains(post.getId()),
                        repostedPostIds.contains(post.getId()));
            }
        }
        if (post.isQuote()) {
            Post original = originalPostMap.get(post.getQuoteId());
            if (original != null) {
                return PostResponse.fromQuote(post, original,
                        likedPostIds.contains(post.getId()),
                        repostedPostIds.contains(post.getId()));
            }
        }
        return PostResponse.from(post,
                likedPostIds.contains(post.getId()),
                repostedPostIds.contains(post.getId()));
    }
}
