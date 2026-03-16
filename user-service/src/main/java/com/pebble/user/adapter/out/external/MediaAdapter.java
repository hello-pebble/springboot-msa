package com.pebble.user.adapter.out.external;

import com.pebble.user.application.port.out.MediaPort;
import com.pebble.user.adapter.in.web.dto.MediaInitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MediaAdapter implements MediaPort {
    private final RestTemplate restTemplate;
    private static final String CORE_SERVICE_URL = "http://localhost:8082/api/v1/media";

    @Override
    public MediaInitResponse initUpload(Long userId, String mediaType, Long fileSize, String fileName) {
        Map<String, Object> request = Map.of(
                "userId", userId,
                "type", mediaType,
                "fileSize", fileSize,
                "fileName", fileName
        );
        return restTemplate.postForObject(CORE_SERVICE_URL + "/init", request, MediaInitResponse.class);
    }

    @Override
    public void completeUpload(Long userId, Long mediaId) {
        Map<String, Object> request = Map.of(
                "userId", userId,
                "mediaId", mediaId
        );
        restTemplate.postForObject(CORE_SERVICE_URL + "/uploaded", request, Void.class);
    }
}
