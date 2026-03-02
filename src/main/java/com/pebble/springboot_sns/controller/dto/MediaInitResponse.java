package com.pebble.springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.media.MediaService;

import java.util.List;

public record MediaInitResponse(
        Long mediaId,
        String uploadId,
        List<String> presignedUrls
) {
    public static MediaInitResponse from(MediaService.UploadInitResult result) {
        return new MediaInitResponse(result.mediaId(), result.uploadId(), result.presignedUrls());
    }
}
