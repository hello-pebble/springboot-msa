package com.pebble.springboot_sns.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostCreateRequest(
        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 280, message = "내용은 280자를 초과할 수 없습니다.")
        String content,
        List<Long> mediaIds
) {
}
