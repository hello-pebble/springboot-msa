package springboot_sns.domain.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostViewService {

    private final PostViewRepository postViewRepository;
    private final PostRepository postRepository;

    public void incrementViewCount(Long postId) {
        postViewRepository.incrementViewCount(postId);
    }

    @Transactional
    public void syncViewCountsToDatabase() {
        Set<String> dirtyPostIds = postViewRepository.getDirtyPostIds();

        if (dirtyPostIds == null || dirtyPostIds.isEmpty()) {
            return;
        }

        List<SyncedPost> syncedPosts = new ArrayList<>();

        for (String postIdStr : dirtyPostIds) {
            try {
                Long postId = Long.parseLong(postIdStr);
                Long viewCount = postViewRepository.getViewCount(postId);

                if (viewCount != null) {
                    syncSinglePost(postId, viewCount);
                    syncedPosts.add(new SyncedPost(postId, postIdStr));
                    log.debug("Synced view count for post {}: {}", postId, viewCount);
                }
            } catch (NumberFormatException e) {
                log.error("Invalid post ID in dirty set: {}", postIdStr, e);
                postViewRepository.removeFromDirtySet(postIdStr);
            } catch (Exception e) {
                log.error("Failed to sync view count for post {}: {}", postIdStr, e.getMessage());
            }
        }

        for (SyncedPost synced : syncedPosts) {
            postViewRepository.deleteViewCount(synced.postId());
            postViewRepository.removeFromDirtySet(synced.postIdStr());
        }
    }

    private void syncSinglePost(Long postId, Long viewCount) {
        postRepository.incrementViewCount(postId, viewCount);
    }

    private record SyncedPost(Long postId, String postIdStr) {}
}
