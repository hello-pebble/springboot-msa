package springboot_sns.domain.timeline;

import com.pebble.springboot_sns.config.TimelineConfig;
import com.pebble.springboot_sns.domain.follow.FollowCount;
import com.pebble.springboot_sns.domain.follow.FollowCountService;
import com.pebble.springboot_sns.domain.follow.FollowService;
import com.pebble.springboot_sns.domain.post.Post;
import com.pebble.springboot_sns.domain.post.PostService;
import com.pebble.springboot_sns.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TimelineService {

    private final TimelineRepository timelineRepository;
    private final FollowService followService;
    private final FollowCountService followCountService;
    private final PostService postService;
    private final TimelineConfig timelineConfig;

    public record TimelineResult(List<Long> postIds, Long nextCursor) {
    }

    public void fanOut(Post post, User author) {
        double score = post.getCreatedAt()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();

        // 저자 자신의 타임라인에 항상 추가
        timelineRepository.add(author.getId(), post.getId(), score);
        timelineRepository.trim(author.getId());

        // celeb이 아닌 경우에만 팔로워에게 fan-out
        FollowCount followCount = followCountService.getByUser(author);
        if (followCount.getFollowerCount() < timelineConfig.celebThreshold()) {
            List<User> followers = followService.getFollowers(author.getId());
            for (User follower : followers) {
                timelineRepository.add(follower.getId(), post.getId(), score);
                timelineRepository.trim(follower.getId());
            }
        }
    }

    public TimelineResult getTimeline(Long userId, Long cursor, int limit) {
        Long scoreCursor = cursor != null
                ? postService.findById(cursor).getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli()
                : null;

        // 1. Redis base 포스트 (postId → epoch millis score)
        int fetchSize = limit + timelineConfig.celebPostLimit();
        Map<Long, Double> baseWithScores = timelineRepository.getPostsWithScore(userId, scoreCursor, fetchSize);

        // 2. 팔로잉 중 celeb 필터
        List<User> followings = followService.getFollowings(userId);
        List<User> celebs = followings.stream()
                .filter(u -> followCountService.getByUser(u).getFollowerCount() >= timelineConfig.celebThreshold())
                .toList();

        // 3. celeb 포스트: cursor 이전 항목만, celebPostLimit개 수집
        Map<Long, Double> celebWithScores = new LinkedHashMap<>();
        int celebLimit = timelineConfig.celebPostLimit();
        for (User celeb : celebs) {
            postService.findByUserExcludingReplies(celeb.getId()).stream()
                    .filter(p -> {
                        double score = p.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli();
                        return scoreCursor == null || score < scoreCursor;
                    })
                    .limit(celebLimit)
                    .forEach(p -> {
                        double score = p.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli();
                        celebWithScores.put(p.getId(), score);
                    });
        }

        // 4. 병합 (중복 시 높은 score 유지)
        Map<Long, Double> merged = new LinkedHashMap<>(celebWithScores);
        baseWithScores.forEach((id, score) -> merged.merge(id, score, Math::max));

        if (merged.isEmpty()) {
            return new TimelineResult(List.of(), null);
        }

        // 5. score 내림차순 정렬, limit개 추출
        List<Map.Entry<Long, Double>> sorted = merged.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit)
                .toList();

        // 6. nextCursor: limit개 미만이면 null, 이상이면 마지막 항목 postId
        Long nextCursor = sorted.size() < limit
                ? null
                : sorted.get(sorted.size() - 1).getKey();

        List<Long> postIds = sorted.stream().map(Map.Entry::getKey).toList();
        return new TimelineResult(postIds, nextCursor);
    }
}
