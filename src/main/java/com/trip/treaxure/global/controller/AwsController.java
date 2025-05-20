package com.trip.treaxure.global.controller;

import com.trip.treaxure.global.dto.PresignedUrlDto;
import com.trip.treaxure.global.dto.PresignedUrlRequest;
import com.trip.treaxure.global.dto.SignedUrlRequest;
import com.trip.treaxure.global.service.CloudFrontService;
import com.trip.treaxure.global.service.CloudWatchService;
import com.trip.treaxure.global.service.S3Service;
import com.trip.treaxure.global.service.SqsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "AWS Services", description = "AWS 관련 API (S3, CloudFront, SQS)")
public class AwsController {

    private final S3Service s3Service;
    private final CloudFrontService cloudFrontService;
    private final CloudWatchService cloudWatchService;
    private final SqsService sqsService;

    /**
     * 이미지 업로드용 S3 Presigned URL을 발급
     * @param request 업로드 요청 정보 (파일명, 콘텐츠 타입)
     * @return Presigned URL 및 관련 경로 정보
     */
    @Operation(
        summary = "S3 Presigned URL 발급",
        description = "이미지 업로드용 S3 Presigned URL을 발급합니다. 클라이언트는 이 URL을 통해 S3에 직접 파일을 업로드할 수 있습니다. " +
                "업로드 완료 후 SQS를 통해 Lambda 함수가 트리거되어 썸네일이 자동 생성됩니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Presigned URL 발급 성공",
                content = @Content(schema = @Schema(implementation = PresignedUrlDto.class))
            )
        }
    )
    @PostMapping("/presigned-upload")
    public ResponseEntity<PresignedUrlDto> getPresignedUrl(
            @Parameter(description = "Presigned URL 요청 정보", required = true)
            @RequestBody PresignedUrlRequest request) {
        // 인증된 사용자 정보 가져오기 (인증 구현 시 활성화)
        String userNickname = getUserInfo().orElse(request.getUserNickname());
        log.info("Presigned URL 발급 요청: 사용자={}, 파일={}", userNickname, request.getFileName());
        
        PresignedUrlDto presignedUrlDto = s3Service.generatePresignedUrl(
                userNickname, 
                request.getFileName(), 
                request.getContentType()
        );
        
        // CloudWatch에 업로드 이벤트 로깅
        cloudWatchService.logUploadEvent(userNickname, request.getContentType());
        
        // SQS에 이미지 처리 메시지 전송 (Lambda 트리거용)
        String messageId = sqsService.sendImageProcessingMessage(
                presignedUrlDto.getBucketName(),
                presignedUrlDto.getOriginalObjectKey(),
                presignedUrlDto.getThumbnailObjectKey()
        );
        log.debug("SQS 메시지 전송 완료: messageId={}", messageId);
        
        return ResponseEntity.ok(presignedUrlDto);
    }

    /**
     * CloudFront를 통한 서명된 URL 발급
     * @param request S3 객체 키 정보
     * @return 서명된 CloudFront URL
     */
    @Operation(
        summary = "CloudFront 서명된 URL 발급",
        description = "S3 객체에 안전하게 접근할 수 있는 CloudFront 서명된 URL을 발급합니다. " +
                "URL은 설정된 만료 시간까지만 유효하며, 만료 후에는 새로운 URL을 요청해야 합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "서명된 URL 발급 성공",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PostMapping("/signed-url")
    public ResponseEntity<Map<String, String>> getSignedUrl(
            @Parameter(description = "서명된 URL 요청 정보", required = true)
            @RequestBody SignedUrlRequest request) {
        String userNickname = getUserInfo().orElse("anonymous");
        log.info("서명된 URL 발급 요청: 사용자={}, 객체키={}", userNickname, request.getObjectKey());
        
        String signedUrl = cloudFrontService.generateSignedUrl(request.getObjectKey());
        
        // CloudWatch에 액세스 이벤트 로깅
        cloudWatchService.logCloudFrontAccess(userNickname, "URL");
        
        // Using HashMap instead of Map.of to avoid type inference issues
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("signedUrl", signedUrl);
        responseMap.put("expiresAt", String.valueOf(System.currentTimeMillis() + cloudFrontService.getExpirationTimeSeconds() * 1000));
        return ResponseEntity.ok(responseMap);
    }

    /**
     * CloudFront 서명된 쿠키 발급 - CloudFront 리소스 접근용
     * @return 서명된 쿠키가 포함된 응답
     */
    @Operation(
        summary = "CloudFront 인증 쿠키 발급",
        description = "CloudFront로 보호된 리소스에 접근하기 위한, 브라우저 쿠키 기반 인증을 설정합니다. " + 
                "응답 헤더에 Set-Cookie가 포함되며, 브라우저는 이후 CloudFront 요청에 이 쿠키를 자동으로 포함시킵니다. " +
                "쿠키는 설정된 만료 시간까지만 유효합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "CloudFront 인증 쿠키 발급 성공 (응답 헤더의 Set-Cookie 확인)",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/auth/cloudfront")
    public ResponseEntity<Map<String, Object>> getCloudFrontCookies() {
        String userNickname = getUserInfo().orElse("anonymous");
        log.info("CloudFront 쿠키 발급 요청: 사용자={}", userNickname);
        
        // CloudFront 쿠키 생성
        Map<String, String> cookies = cloudFrontService.generateSignedCookies();
        
        // CloudWatch에 액세스 이벤트 로깅
        cloudWatchService.logCloudFrontAccess(userNickname, "Cookie");
        
        // 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        cookies.forEach((name, value) -> {
            log.debug("쿠키 추가: {}={}", name, value);
            headers.add(HttpHeaders.SET_COOKIE, value);
        });
        
        // 만료 시간 계산
        long expirationTimeMillis = System.currentTimeMillis() + cloudFrontService.getExpirationTimeSeconds() * 1000;
        
        // 응답 바디 생성
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "CloudFront 인증 완료");
        responseBody.put("expiresAt", expirationTimeMillis);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody);
    }
    
    /**
     * 현재 인증된 사용자 정보 가져오기
     * @return 사용자 닉네임 (Optional)
     */
    private Optional<String> getUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && 
                    !"anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.of(authentication.getName());
            }
        } catch (Exception e) {
            log.warn("사용자 인증 정보 조회 실패", e);
        }
        return Optional.empty();
    }
} 