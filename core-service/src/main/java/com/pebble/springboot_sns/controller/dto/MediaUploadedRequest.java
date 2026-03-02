package com.pebble.springboot_sns.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MediaUploadedRequest(
        @NotNull(message = "미디어 ID를 입력해주세요.")
        Long mediaId,

        List<PartInfo> parts
) {
    public record PartInfo(
            int partNumber,
            @JsonProperty("eTag") String etag
    ) {
    }
}
