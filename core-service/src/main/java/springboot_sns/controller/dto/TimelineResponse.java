package springboot_sns.controller.dto;

import java.util.List;

public record TimelineResponse(
        List<PostResponse> posts,
        Long nextCursor
) {
}
