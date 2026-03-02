package com.pebble.springboot_sns.domain.media;

import com.pebble.springboot_sns.config.StorageConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final StorageConfig storageConfig;

    @PostConstruct
    public void init() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(storageConfig.bucket())
                    .build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(storageConfig.bucket())
                    .build());
        }
    }

    public String generatePresignedGetUrl(String key) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(storageConfig.bucket())
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toExternalForm();
    }

    public String generatePresignedPutUrl(String key) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(storageConfig.bucket())
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toExternalForm();
    }

    public String createMultipartUpload(String key) {
        CreateMultipartUploadRequest request = CreateMultipartUploadRequest.builder()
                .bucket(storageConfig.bucket())
                .key(key)
                .build();

        return s3Client.createMultipartUpload(request).uploadId();
    }

    public String generatePresignedUploadPartUrl(String key, String uploadId, int partNumber) {
        UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                .bucket(storageConfig.bucket())
                .key(key)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .build();

        UploadPartPresignRequest presignRequest = UploadPartPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .uploadPartRequest(uploadPartRequest)
                .build();

        PresignedUploadPartRequest presignedRequest = s3Presigner.presignUploadPart(presignRequest);
        return presignedRequest.url().toExternalForm();
    }

    public void completeMultipartUpload(String key, String uploadId, List<CompletedPart> parts) {
        CompletedMultipartUpload completedUpload = CompletedMultipartUpload.builder()
                .parts(parts)
                .build();

        s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(storageConfig.bucket())
                .key(key)
                .uploadId(uploadId)
                .multipartUpload(completedUpload)
                .build());
    }
}
