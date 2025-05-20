package com.trip.treaxure.global.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsService {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    @Value("${aws.sqs.queue-url:}")
    private String queueUrl;

    @Value("${aws.sqs.disabled:false}")
    private boolean sqsDisabled;

    /**
     * 이미지 처리 메시지를 SQS에 전송
     * @param bucketName S3 버킷 이름
     * @param objectKey S3 객체 키
     * @param thumbnailObjectKey 썸네일 객체 키
     * @return 메시지 ID
     */
    public String sendImageProcessingMessage(String bucketName, String objectKey, String thumbnailObjectKey) {
        Map<String, String> messageBody = new HashMap<>();
        messageBody.put("bucketName", bucketName);
        messageBody.put("objectKey", objectKey);
        messageBody.put("thumbnailObjectKey", thumbnailObjectKey);
        messageBody.put("action", "PROCESS_IMAGE");
        
        try {
            String messageBodyJson = objectMapper.writeValueAsString(messageBody);
            
            // 테스트 모드이거나 SQS가 비활성화된 경우 로그만 출력하고 더미 ID 반환
            if (isTestProfile() || sqsDisabled || queueUrl.isEmpty()) {
                log.info("SQS 비활성화 또는 테스트 모드: 메시지 전송 건너뜀 - {}", messageBodyJson);
                return UUID.randomUUID().toString();
            }
            
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBodyJson)
                    .delaySeconds(5) // 5초 지연
                    .build();
            
            SendMessageResponse response = sqsClient.sendMessage(sendMessageRequest);
            return response.messageId();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("SQS 메시지 생성 실패", e);
        }
    }

    /**
     * 이미지 처리 완료 알림 메시지를 SQS에 전송
     * @param objectKey 처리된 S3 객체 키
     * @param status 처리 상태
     * @return 메시지 ID
     */
    public String sendProcessingCompletionMessage(String objectKey, String status) {
        Map<String, String> messageBody = new HashMap<>();
        messageBody.put("objectKey", objectKey);
        messageBody.put("status", status);
        messageBody.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        try {
            String messageBodyJson = objectMapper.writeValueAsString(messageBody);
            
            // 테스트 모드이거나 SQS가 비활성화된 경우 로그만 출력하고 더미 ID 반환
            if (isTestProfile() || sqsDisabled || queueUrl.isEmpty()) {
                log.info("SQS 비활성화 또는 테스트 모드: 메시지 전송 건너뜀 - {}", messageBodyJson);
                return UUID.randomUUID().toString();
            }
            
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBodyJson)
                    .build();
            
            SendMessageResponse response = sqsClient.sendMessage(sendMessageRequest);
            return response.messageId();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("SQS 메시지 생성 실패", e);
        }
    }
    
    /**
     * 현재 실행 환경이 테스트 프로파일인지 확인
     */
    private boolean isTestProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains("test");
    }
} 