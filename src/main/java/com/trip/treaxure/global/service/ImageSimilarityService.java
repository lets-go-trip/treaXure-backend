package com.trip.treaxure.global.service;

/**
 * 이미지 유사도 계산을 위한 서비스 인터페이스
 */
public interface ImageSimilarityService {
    
    /**
     * 두 이미지 URL의 유사도를 계산합니다.
     * 
     * @param referenceImageUrl 기준 이미지 URL
     * @param targetImageUrl 비교 대상 이미지 URL
     * @return 0~1 사이의 유사도 점수 (1에 가까울수록 유사)
     */
    float compare(String referenceImageUrl, String targetImageUrl);
} 