package com.trip.treaxure.global.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.trip.treaxure.global.util.ImageUtils;

@ExtendWith(MockitoExtension.class)
class ImageSimilarityServiceTest {

    @Mock
    private ImageUtils imageUtils;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenAiImageSimilarityService imageSimilarityService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(imageSimilarityService, "similarityThreshold", 0.75f);
        ReflectionTestUtils.setField(imageSimilarityService, "openaiApiKey", "test-api-key");
        ReflectionTestUtils.setField(imageSimilarityService, "openaiApiUrl", "https://api.openai.com/v1");
    }

    @Test
    void testCompare_ShouldReturnSimilarityScore() throws IOException {
        // Given
        String referenceUrl = "https://example.com/reference.jpg";
        String targetUrl = "https://example.com/target.jpg";
        
        byte[] referenceBytes = "reference image data".getBytes();
        byte[] targetBytes = "target image data".getBytes();
        
        when(imageUtils.loadImageFromUrl(referenceUrl)).thenReturn(referenceBytes);
        when(imageUtils.loadImageFromUrl(targetUrl)).thenReturn(targetBytes);
        
        // When
        float similarity = imageSimilarityService.compare(referenceUrl, targetUrl);
        
        // Then
        assertTrue(similarity >= 0 && similarity <= 1, "Similarity score should be between 0 and 1");
        verify(imageUtils, times(1)).loadImageFromUrl(referenceUrl);
        verify(imageUtils, times(1)).loadImageFromUrl(targetUrl);
    }
    
    @Test
    void testCompare_WhenImageLoadingFails_ShouldThrowException() throws IOException {
        // Given
        String referenceUrl = "https://example.com/reference.jpg";
        String targetUrl = "https://example.com/target.jpg";
        
        when(imageUtils.loadImageFromUrl(referenceUrl)).thenThrow(new IOException("Failed to load image"));
        
        // When / Then
        assertThrows(RuntimeException.class, () -> {
            imageSimilarityService.compare(referenceUrl, targetUrl);
        });
    }
    
    /**
     * OpenAI Embedding API 응답 형태를 생성합니다.
     */
    private Map<String, Object> createMockEmbeddingResponse(float[] embedding) {
        Map<String, Object> embeddingMap = new HashMap<>();
        embeddingMap.put("embedding", List.of(embedding[0], embedding[1], embedding[2]));
        
        Map<String, Object> dataItem = new HashMap<>();
        dataItem.put("embedding", List.of(embedding[0], embedding[1], embedding[2]));
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", List.of(dataItem));
        
        return response;
    }
} 