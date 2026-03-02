package springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.follow.FollowCount;

public record FollowCountResponse(
        Long followerCount,
        Long followingCount
) {
    public static FollowCountResponse from(FollowCount followCount) {
        return new FollowCountResponse(
                followCount.getFollowerCount(),
                followCount.getFollowingCount()
        );
    }
}
