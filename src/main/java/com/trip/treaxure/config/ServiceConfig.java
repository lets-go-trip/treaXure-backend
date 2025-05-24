package com.trip.treaxure.config;

import com.trip.treaxure.global.service.ImageSimilarityService;
import com.trip.treaxure.global.service.OpenAiVisionSimilarityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    /**
     * 기본 이미지 유사도 서비스를 정의합니다.
     * 여러 구현체 중 OpenAI Vision API를 우선적으로 사용합니다.
     */
    @Bean
    public ImageSimilarityService imageSimilarityService(OpenAiVisionSimilarityService openAiVisionSimilarityService) {
        return openAiVisionSimilarityService;
    }
} 