package springboot_sns.controller.dto;

import com.pebble.springboot_sns.domain.media.MediaType;

public record MediaPresignedUrlResponse(String presignedUrl, MediaType mediaType) {
}
