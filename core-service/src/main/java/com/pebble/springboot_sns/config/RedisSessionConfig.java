package com.pebble.springboot_sns.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@Profile("!test")
@EnableRedisIndexedHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {

    @Bean
    public <S extends Session> SpringSessionBackedSessionRegistry<S> sessionRegistry(
            FindByIndexNameSessionRepository<S> sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }
}
