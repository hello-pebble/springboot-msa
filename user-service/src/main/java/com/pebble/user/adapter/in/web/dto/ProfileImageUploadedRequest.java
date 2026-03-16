package com.pebble.user.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

public record ProfileImageUploadedRequest(
        @NotNull Long mediaId
) {
}
