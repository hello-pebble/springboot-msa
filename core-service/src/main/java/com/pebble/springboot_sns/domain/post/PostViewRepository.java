package com.pebble.springboot_sns.domain.post;

import com.pebble.springboot_sns.config.PostViewConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PostViewRepository {

    private final StringRedisTemplate redisTemplate;
    private final PostViewConfig postViewConfig;

    private static final String INCREMENT_SCRIPT =
            "redis.call('INCR', KEYS[1]); " +
            "redis.call('SADD', KEYS[2], ARGV[1]); " +
            "return 1";

    public void incrementViewCount(Long postId) {
        String viewCountKey = postViewConfig.keyPrefix() + postId;
        String dirtySetKey = postViewConfig.dirtySetKey();

        DefaultRedisScript<Long> script = new DefaultRedisScript<>(INCREMENT_SCRIPT, Long.class);
        redisTemplate.execute(script, List.of(viewCountKey, dirtySetKey), postId.toString());
    }

    public Long getViewCount(Long postId) {
        String value = redisTemplate.opsForValue().get(postViewConfig.keyPrefix() + postId);
        return value != null ? Long.parseLong(value) : null;
    }

    public Set<String> getDirtyPostIds() {
        return redisTemplate.opsForSet().members(postViewConfig.dirtySetKey());
    }

    public void deleteViewCount(Long postId) {
        redisTemplate.delete(postViewConfig.keyPrefix() + postId);
    }

    public void removeFromDirtySet(String postId) {
        redisTemplate.opsForSet().remove(postViewConfig.dirtySetKey(), postId);
    }
}
