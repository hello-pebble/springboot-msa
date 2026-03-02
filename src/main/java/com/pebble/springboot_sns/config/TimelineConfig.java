package com.pebble.springboot_sns.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "timeline")
public record TimelineConfig(
        String keyPrefix,
        long maxSize,
        long celebThreshold,
        int celebPostLimit
) {
}
