package com.pebble.springboot_sns.controller.dto;

import jakarta.validation.constraints.NotNull;

public record ProfileImageUploadedRequest(
        @NotNull Long mediaId
) {
}
