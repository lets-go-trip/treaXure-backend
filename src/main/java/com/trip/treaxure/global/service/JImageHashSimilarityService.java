package com.trip.treaxure.global.service;

import com.trip.treaxure.global.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 이미지 유사도 계산 서비스 - 임시 구현
 * 추후 JImageHash 라이브러리를 정상적으로 연동하여 실제 유사도 계산을 구현할 예정입니다.
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class JImageHashSimilarityService implements ImageSimilarityService {

    private final ImageUtils imageUtils;
    
    @Value("${image.similarity.threshold:0.75}")
    private float similarityThreshold;

    /**
     * 두 이미지의 유사도를 계산합니다.
     * 현재는 임시 구현으로, 실제 유사도 계산은 수행하지 않습니다.
     *
     * @param referenceImageUrl 기준 이미지 URL
     * @param targetImageUrl 비교 대상 이미지 URL
     * @return 0~1 사이의 유사도 점수 (1에 가까울수록 유사)
     */
    @Override
    public float compare(String referenceImageUrl, String targetImageUrl) {
        try {
            // 이미지 로드 확인 (실제 비교는 수행하지 않음)
            byte[] referenceImageBytes = imageUtils.loadImageFromUrl(referenceImageUrl);
            byte[] targetImageBytes = imageUtils.loadImageFromUrl(targetImageUrl);
            
            // 현재는 임시 구현: 이미지 URL로부터 일정한 유사도 점수를 생성
            // 실제 구현 시에는 적절한 알고리즘을 사용해야 함
            float similarity = generateTemporarySimilarity(referenceImageUrl, targetImageUrl);
            
            log.info("Image similarity between {} and {}: {} (temporary implementation)", 
                    referenceImageUrl, targetImageUrl, similarity);
            
            return similarity;
            
        } catch (IOException e) {
            log.error("Error comparing images: {} and {}", referenceImageUrl, targetImageUrl, e);
            throw new RuntimeException("이미지 유사도 계산 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 임시 구현: 두 URL 기반으로 일정한 유사도 점수를 생성
     * 실제 구현 시 이 메소드는 제거되고 실제 알고리즘이 적용됨
     */
    private float generateTemporarySimilarity(String url1, String url2) {
        // 두 URL 문자열의 해시코드를 사용하여 일정한 랜덤 값 생성
        // 같은 입력값에 대해 항상 같은 결과를 반환
        int combinedHash = url1.hashCode() + url2.hashCode();
        Random random = new Random(combinedHash);
        
        // 0.5 ~ 1.0 사이의 값을 반환 (테스트 목적)
        return 0.5f + (random.nextFloat() * 0.5f);
    }
} 