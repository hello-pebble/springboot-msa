package com.pebble.springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.media.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MediaInitRequest(
        @NotNull(message = "미디어 타입을 입력해주세요.")
        MediaType mediaType,

        @NotNull(message = "파일 크기를 입력해주세요.")
        @Positive(message = "파일 크기는 0보다 커야 합니다.")
        Long fileSize,

        @NotBlank(message = "파일명을 입력해주세요.")
        String fileName
) {
}
