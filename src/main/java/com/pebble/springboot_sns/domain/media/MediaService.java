package com.pebble.springboot_sns.domain.media;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.CompletedPart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final StorageService storageService;

    private static final long PART_SIZE = 8 * 1024 * 1024; // 8MB

    public record UploadInitResult(Long mediaId, String uploadId, List<String> presignedUrls) {
    }

    public record PartInfo(int partNumber, String etag) {
    }

    public UploadInitResult initUpload(Long userId, MediaType mediaType, long fileSize, String fileName) {
        String extension = getExtension(fileName);
        String key = userId + "/" + UUID.randomUUID() + "." + extension;

        Media media;
        List<String> presignedUrls = new ArrayList<>();

        if (fileSize <= PART_SIZE) {
            media = new Media(mediaType, key, MediaStatus.INIT, userId);
            mediaRepository.save(media);
            presignedUrls.add(storageService.generatePresignedPutUrl(key));
        } else {
            String uploadId = storageService.createMultipartUpload(key);
            media = new Media(mediaType, key, MediaStatus.INIT, userId, uploadId);
            mediaRepository.save(media);

            int partCount = (int) Math.ceil((double) fileSize / PART_SIZE);
            for (int i = 1; i <= partCount; i++) {
                presignedUrls.add(storageService.generatePresignedUploadPartUrl(key, uploadId, i));
            }
        }

        return new UploadInitResult(media.getId(), media.getUploadId(), presignedUrls);
    }

    @Transactional
    public void completeUpload(Long userId, Long mediaId, List<PartInfo> parts) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(mediaId)
                .orElseThrow(() -> new MediaException("미디어를 찾을 수 없습니다."));

        if (!media.getUserId().equals(userId)) {
            throw new MediaException("미디어에 대한 권한이 없습니다.");
        }

        if (media.getStatus() != MediaStatus.INIT) {
            throw new MediaException("업로드 초기화 상태의 미디어만 완료할 수 있습니다.");
        }

        if (media.getUploadId() != null) {
            if (parts == null || parts.isEmpty()) {
                throw new MediaException("멀티파트 업로드에는 파트 정보가 필요합니다.");
            }
            List<CompletedPart> completedParts = parts.stream()
                    .sorted(Comparator.comparingInt(PartInfo::partNumber))
                    .map(p -> CompletedPart.builder()
                            .partNumber(p.partNumber())
                            .eTag(normalizeETag(p.etag()))
                            .build())
                    .toList();
            storageService.completeMultipartUpload(media.getPath(), media.getUploadId(), completedParts);
        }

        media.updateStatus(MediaStatus.UPLOADED);
    }

    public record MediaUrlResult(String presignedUrl, MediaType mediaType) {
    }

    public MediaUrlResult getPresignedUrl(Long mediaId) {
        Media media = mediaRepository.findByIdAndDeletedAtIsNull(mediaId)
                .orElseThrow(() -> new MediaException("미디어를 찾을 수 없습니다."));

        if (media.getStatus() != MediaStatus.UPLOADED) {
            throw new MediaException("업로드가 완료된 미디어만 조회할 수 있습니다.");
        }

        String url = storageService.generatePresignedGetUrl(media.getPath());
        return new MediaUrlResult(url, media.getMediaType());
    }

    private String normalizeETag(String etag) {
        if (etag != null && !etag.startsWith("\"")) {
            return "\"" + etag + "\"";
        }
        return etag;
    }

    private String getExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }
}
