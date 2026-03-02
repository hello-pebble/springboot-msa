package springboot_sns.domain.timeline;

import com.pebble.springboot_sns.config.TimelineConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TimelineRepository {

    private final StringRedisTemplate redisTemplate;
    private final TimelineConfig timelineConfig;

    public void add(Long userId, Long postId, double score) {
        String key = timelineConfig.keyPrefix() + userId;
        redisTemplate.opsForZSet().add(key, postId.toString(), score);
    }

    /**
     * 커서 기반으로 postId → score(epoch millis) 맵 반환 (score 내림차순)
     * cursor == null: 최신 limit개
     * cursor != null: score < cursor 범위에서 limit개
     */
    public Map<Long, Double> getPostsWithScore(Long userId, Long cursor, int limit) {
        String key = timelineConfig.keyPrefix() + userId;
        Set<ZSetOperations.TypedTuple<String>> raw;

        if (cursor == null) {
            raw = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, limit - 1);
        } else {
            raw = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(
                    key, Double.NEGATIVE_INFINITY, cursor - 1, 0, limit);
        }

        if (raw == null || raw.isEmpty()) {
            return Map.of();
        }

        Map<Long, Double> result = new LinkedHashMap<>();
        for (ZSetOperations.TypedTuple<String> tuple : raw) {
            if (tuple.getValue() != null && tuple.getScore() != null) {
                result.put(Long.parseLong(tuple.getValue()), tuple.getScore());
            }
        }
        return result;
    }

    public void remove(Long userId, Long postId) {
        String key = timelineConfig.keyPrefix() + userId;
        redisTemplate.opsForZSet().remove(key, postId.toString());
    }

    public void trim(Long userId) {
        String key = timelineConfig.keyPrefix() + userId;
        long maxSize = timelineConfig.maxSize();
        redisTemplate.opsForZSet().removeRange(key, 0, -(maxSize + 1));
    }
}
