package com.trip.treaxure.global.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.treaxure.global.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAI Vision API를 활용한 이미지 유사도 계산 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiVisionSimilarityService implements ImageSimilarityService {

    private final ImageUtils imageUtils;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${openai.api.key}")
    private String openaiApiKey;
    
    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String openaiApiUrl;
    
    @Value("${openai.api.model:gpt-4o-mini}")
    private String openaiModel;
    
    /**
     * 두 이미지의 유사도를 계산합니다.
     *
     * @param referenceImageUrl 기준 이미지 URL
     * @param targetImageUrl 비교 대상 이미지 URL
     * @return 0~1 사이의 유사도 점수 (1에 가까울수록 유사)
     */
    @Override
    public float compare(String referenceImageUrl, String targetImageUrl) {
        try {
            // 이미지 로드 및 Base64 인코딩
            byte[] referenceImageBytes = imageUtils.loadImageFromUrl(referenceImageUrl);
            byte[] targetImageBytes = imageUtils.loadImageFromUrl(targetImageUrl);
            
            String referenceBase64 = Base64.getEncoder().encodeToString(referenceImageBytes);
            String targetBase64 = Base64.getEncoder().encodeToString(targetImageBytes);
            
            // 이미지 설명 추출
            String referenceDescription = getImageDescription(referenceBase64, imageUtils.getContentType(referenceImageUrl));
            String targetDescription = getImageDescription(targetBase64, imageUtils.getContentType(targetImageUrl));
            
            log.info("Reference image description: {}", referenceDescription);
            log.info("Target image description: {}", targetDescription);
            
            // 이미지 설명 유사도 평가
            float similarityScore = compareDescriptions(referenceDescription, targetDescription);
            
            log.info("Image similarity between {} and {}: {}", referenceImageUrl, targetImageUrl, similarityScore);
            return similarityScore;
            
        } catch (IOException e) {
            log.error("Error comparing images: {} and {}", referenceImageUrl, targetImageUrl, e);
            throw new RuntimeException("이미지 유사도 계산 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 이미지의 설명을 OpenAI Vision API를 통해 얻습니다.
     *
     * @param imageBase64 Base64로 인코딩된 이미지
     * @param contentType 이미지의 Content-Type
     * @return 이미지 설명
     */
    private String getImageDescription(String imageBase64, String contentType) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);
        
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Analyze this image in detail and provide a description that captures key visual elements.");
        
        Map<String, Object> imageUrl = new HashMap<>();
        imageUrl.put("url", "data:" + contentType + ";base64," + imageBase64);
        
        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        imageContent.put("image_url", imageUrl);
        
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", List.of(imageContent));
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openaiModel);
        requestBody.put("messages", Arrays.asList(systemMessage, userMessage));
        requestBody.put("max_tokens", 300);
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        String responseJson = restTemplate.postForObject(openaiApiUrl, requestEntity, String.class);
        JsonNode responseNode = objectMapper.readTree(responseJson);
        
        if (responseNode.has("error")) {
            String errorMessage = responseNode.get("error").get("message").asText();
            log.error("Error from OpenAI API: {}", errorMessage);
            throw new RuntimeException("OpenAI API 오류: " + errorMessage);
        }
        
        return responseNode.get("choices").get(0).get("message").get("content").asText();
    }
    
    /**
     * 두 이미지 설명의 유사도를 OpenAI API를 통해 계산합니다.
     *
     * @param referenceDescription 기준 이미지 설명
     * @param targetDescription 비교 대상 이미지 설명
     * @return 0~1 사이의 유사도 점수
     */
    private float compareDescriptions(String referenceDescription, String targetDescription) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);
        
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are an image similarity assessment expert. Calculate a similarity score between 0 and 1 based on the descriptions of two images.");
        
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "I have two image descriptions. Please analyze them and give me a similarity score between 0 and 1, " +
                "where 1 means they are identical and 0 means completely different. Only respond with a number between 0 and 1, " +
                "with up to 4 decimal places.\n\nFirst image: " + referenceDescription + "\n\nSecond image: " + targetDescription);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openaiModel);
        requestBody.put("messages", Arrays.asList(systemMessage, userMessage));
        requestBody.put("max_tokens", 10);
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        String responseJson = restTemplate.postForObject(openaiApiUrl, requestEntity, String.class);
        JsonNode responseNode = objectMapper.readTree(responseJson);
        
        if (responseNode.has("error")) {
            String errorMessage = responseNode.get("error").get("message").asText();
            log.error("Error from OpenAI API: {}", errorMessage);
            throw new RuntimeException("OpenAI API 오류: " + errorMessage);
        }
        
        String scoreText = responseNode.get("choices").get(0).get("message").get("content").asText().trim();
        try {
            return Float.parseFloat(scoreText);
        } catch (NumberFormatException e) {
            log.error("Invalid similarity score format from API: {}", scoreText);
            throw new RuntimeException("OpenAI API가 유효한 유사도 점수를 반환하지 않았습니다: " + scoreText);
        }
    }
} 