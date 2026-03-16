package com.pebble.user.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProfileImageInitRequest(
        @NotNull @Positive Long fileSize,
        @NotNull String fileName
) {
}
