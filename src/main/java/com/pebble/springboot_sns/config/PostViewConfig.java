package com.pebble.springboot_sns.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "post.view")
public record PostViewConfig(
        String keyPrefix,
        String dirtySetKey
) {
    public PostViewConfig {
        if (keyPrefix == null) {
            keyPrefix = "post:view:";
        }
        if (dirtySetKey == null) {
            dirtySetKey = "post:view:dirty_set";
        }
    }
}
