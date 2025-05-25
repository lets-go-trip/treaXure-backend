package com.trip.treaxure.global.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.treaxure.global.dto.PresignedUrlDto;
import com.trip.treaxure.global.dto.PresignedUrlRequest;
import com.trip.treaxure.global.dto.SignedUrlRequest;
import com.trip.treaxure.global.service.CloudFrontService;
import com.trip.treaxure.global.service.CloudWatchService;
import com.trip.treaxure.global.service.S3Service;
import com.trip.treaxure.global.service.SqsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AwsControllerTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private CloudFrontService cloudFrontService;
    
    @Mock
    private CloudWatchService cloudWatchService;
    
    @Mock
    private SqsService sqsService;
    
    @InjectMocks
    private AwsController awsController;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    @DisplayName("Presigned URL 발급 API 테스트")
    void getPresignedUrlTest() throws Exception {
        // Setup MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(awsController).build();
        
        // Given
        PresignedUrlRequest request = PresignedUrlRequest.builder()
                .userNickname("testUser")
                .fileName("test.jpg")
                .contentType("image/jpeg")
                .build();

        PresignedUrlDto response = PresignedUrlDto.builder()
                .presignedUrl("https://test-bucket.s3.ap-northeast-2.amazonaws.com/...")
                .originalObjectKey("images/missions/testUser/1234567890-abcdef.jpg")
                .thumbnailObjectKey("images/missions/testUser/1234567890-abcdef_thumbnail.jpg")
                .bucketName("test-bucket")
                .build();

        // Mock services
        when(s3Service.generatePresignedUrl(anyString(), anyString(), anyString())).thenReturn(response);
        when(sqsService.sendImageProcessingMessage(anyString(), anyString(), anyString())).thenReturn("test-message-id");
        // Mock CloudWatchService to avoid UnnecessaryStubbingException
        doNothing().when(cloudWatchService).logUploadEvent(anyString(), anyString());
        
        // When & Then
        mockMvc.perform(post("/api/presigned-upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.presignedUrl").exists())
                .andExpect(jsonPath("$.originalObjectKey").exists())
                .andExpect(jsonPath("$.thumbnailObjectKey").exists())
                .andExpect(jsonPath("$.bucketName").value("test-bucket"));
    }

    @Test
    @DisplayName("서명된 URL 발급 API 테스트")
    void getSignedUrlTest() throws Exception {
        // Setup MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(awsController).build();
        
        // Setup expiration time for CloudFront
        when(cloudFrontService.getExpirationTimeSeconds()).thenReturn(3600L);
        
        // Given
        SignedUrlRequest request = SignedUrlRequest.builder()
                .objectKey("images/testUser/1234567890-abcdef.jpg")
                .build();

        // CloudFrontService 모킹
        when(cloudFrontService.generateSignedUrl(anyString()))
                .thenReturn("https://test-cloudfront.cloudfront.net/images/...");
        // Mock CloudWatchService
        doNothing().when(cloudWatchService).logCloudFrontAccess(anyString(), anyString());

        // When & Then
        mockMvc.perform(post("/api/signed-url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.signedUrl").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    @DisplayName("CloudFront 서명된 쿠키 발급 API 테스트")
    void getCloudFrontCookiesTest() throws Exception {
        // Setup MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(awsController).build();
        
        // Setup expiration time for CloudFront
        when(cloudFrontService.getExpirationTimeSeconds()).thenReturn(3600L);
        
        // Given
        Map<String, String> cookieMap = new HashMap<>();
        cookieMap.put("CloudFront-Key-Pair-Id", "CloudFront-Key-Pair-Id=Cookie-Value-1");
        cookieMap.put("CloudFront-Policy", "CloudFront-Policy=Cookie-Value-2");
        cookieMap.put("CloudFront-Signature", "CloudFront-Signature=Cookie-Value-3");

        // CloudFrontService 모킹
        when(cloudFrontService.generateSignedCookies()).thenReturn(cookieMap);
        // Mock CloudWatchService
        doNothing().when(cloudWatchService).logCloudFrontAccess(anyString(), anyString());

        // When & Then
        mockMvc.perform(get("/api/auth/cloudfront"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("CloudFront 인증 완료"))
                .andExpect(jsonPath("$.expiresAt").exists());
    }
} 