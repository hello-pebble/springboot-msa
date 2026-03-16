package com.pebble.user.application.port.out;

import com.pebble.user.adapter.in.web.dto.MediaInitResponse;

public interface MediaPort {
    MediaInitResponse initUpload(Long userId, String mediaType, Long fileSize, String fileName);
    void completeUpload(Long userId, Long mediaId);
}
