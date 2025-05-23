package com.trip.treaxure.global.service;

import com.trip.treaxure.global.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.Base64;

/**
 * OpenAI API를 활용한 이미지 유사도 계산 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiImageSimilarityService implements ImageSimilarityService {

    private final ImageUtils imageUtils;
    private final RestTemplate restTemplate;
    
    @Value("${image.similarity.threshold:0.75}")
    private float similarityThreshold;
    
    @Value("${openai.api.key:dummy-key}")
    private String openaiApiKey;
    
    @Value("${openai.api.url:https://api.openai.com/v1}")
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
            // 이미지 로드
            byte[] referenceImageBytes = imageUtils.loadImageFromUrl(referenceImageUrl);
            byte[] targetImageBytes = imageUtils.loadImageFromUrl(targetImageUrl);
            
            // 이미지 임베딩 추출
            float[] referenceEmbedding = extractImageEmbedding(referenceImageBytes);
            float[] targetEmbedding = extractImageEmbedding(targetImageBytes);
            
            // 코사인 유사도 계산
            float similarity = calculateCosineSimilarity(referenceEmbedding, targetEmbedding);
            
            log.info("Image similarity between {} and {}: {}", referenceImageUrl, targetImageUrl, similarity);
            return similarity;
            
        } catch (IOException e) {
            log.error("Error comparing images: {} and {}", referenceImageUrl, targetImageUrl, e);
            throw new RuntimeException("이미지 유사도 계산 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 이미지 바이트 배열에서 임베딩을 추출합니다.
     *
     * @param imageBytes 이미지 바이트 배열
     * @return 이미지 임베딩 (float 배열)
     */
    private float[] extractImageEmbedding(byte[] imageBytes) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            
            // 이 부분은 실제 OpenAI API 호출을 구현해야 하지만, 아직 API가 준비되지 않았으므로 더미 임베딩을 반환합니다.
            // 실제 구현 시에는 API 호출을 통해 임베딩을 얻어야 합니다.
            log.debug("OpenAI API would be called to get embedding for image (size: {} bytes)", imageBytes.length);
            
            // 더미 임베딩 생성 (실제 API 연동 시 이 부분을 교체해야 함)
            float[] dummyEmbedding = new float[512];
            Random random = new Random(Arrays.hashCode(imageBytes)); // 이미지 내용에 기반한 시드 사용
            for (int i = 0; i < dummyEmbedding.length; i++) {
                dummyEmbedding[i] = random.nextFloat();
            }
            
            return dummyEmbedding;
        } catch (Exception e) {
            log.error("Error extracting image embedding", e);
            throw new RuntimeException("이미지 임베딩 추출 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 두 벡터 간의 코사인 유사도를 계산합니다.
     *
     * @param vectorA 첫 번째 벡터
     * @param vectorB 두 번째 벡터
     * @return 코사인 유사도 (0~1 사이의 값)
     */
    private float calculateCosineSimilarity(float[] vectorA, float[] vectorB) {
        // 수동으로 float[]를 double[]로 변환
        double[] doubleVectorA = new double[vectorA.length];
        double[] doubleVectorB = new double[vectorB.length];
        
        for (int i = 0; i < vectorA.length; i++) {
            doubleVectorA[i] = vectorA[i];
        }
        
        for (int i = 0; i < vectorB.length; i++) {
            doubleVectorB[i] = vectorB[i];
        }
        
        RealVector v1 = new ArrayRealVector(doubleVectorA);
        RealVector v2 = new ArrayRealVector(doubleVectorB);
        
        double dotProduct = v1.dotProduct(v2);
        double normA = v1.getNorm();
        double normB = v2.getNorm();
        
        // 0으로 나누는 것을 방지
        if (normA == 0 || normB == 0) {
            return 0;
        }
        
        double similarity = dotProduct / (normA * normB);
        
        // 결과를 0~1 사이로 정규화 (코사인 유사도는 -1~1 사이의 값)
        return (float) ((similarity + 1) / 2);
    }
} 