package com.trip.treaxure.global.service;

import com.trip.treaxure.global.dto.PresignedUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.image-path-prefix}")
    private String imagePathPrefix;

    @Value("${aws.region}")
    private String region;
    
    @Value("${aws.access-key}")
    private String accessKey;
    
    @Value("${aws.secret-key}")
    private String secretKey;

    /**
     * 프론트엔드에서 S3에 직접 업로드할 수 있도록 Presigned URL을 생성
     * @param userNickname 사용자 닉네임
     * @param fileName 원본 파일명
     * @param contentType 파일 콘텐츠 타입 (예: image/jpeg)
     * @return S3 Presigned URL 및 경로 정보
     */
    public PresignedUrlDto generatePresignedUrl(String userNickname, String fileName, String contentType) {
        // 파일명 충돌 방지를 위해 타임스탬프와 UUID 추가
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileExtension = getFileExtension(fileName);
        String s3ObjectKey = String.format("%s/%s/%s-%s%s", 
                                        imagePathPrefix, 
                                        userNickname, 
                                        timestamp, 
                                        UUID.randomUUID().toString().substring(0, 8), 
                                        fileExtension);

        // Presigner 생성
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)))
                .build()) {

            // Presigned URL 요청 설정
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3ObjectKey)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15)) // URL 유효 시간
                    .putObjectRequest(objectRequest)
                    .build();

            // Presigned URL 생성
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            // 썸네일 경로 (Lambda가 생성할 썸네일 파일 경로)
            String thumbnailObjectKey = s3ObjectKey.replace(fileExtension, "_thumbnail" + fileExtension);

            return PresignedUrlDto.builder()
                    .presignedUrl(presignedRequest.url().toString())
                    .originalObjectKey(s3ObjectKey)
                    .thumbnailObjectKey(thumbnailObjectKey)
                    .bucketName(bucketName)
                    .build();
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOf);
    }
} 