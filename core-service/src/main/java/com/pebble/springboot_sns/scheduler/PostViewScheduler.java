package com.pebble.springboot_sns.scheduler;

import com.pebble.springboot_sns.domain.post.PostViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostViewScheduler {

    private final PostViewService postViewService;

    @Scheduled(fixedRateString = "${post.view.sync-interval:60000}")
    public void syncViewCountsToDatabase() {
        postViewService.syncViewCountsToDatabase();
    }
}
