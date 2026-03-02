package springboot_sns.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public record StorageConfig(
        String endpoint,
        String accessKey,
        String secretKey,
        String bucket,
        String region
) {
}
