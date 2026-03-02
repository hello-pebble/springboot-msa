package springboot_sns.controller.dto;

import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
        @Size(max = 50) String displayName,
        @Size(max = 160) String bio,
        Long profileImageId,
        Long headerImageId
) {
}
