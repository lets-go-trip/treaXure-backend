package com.trip.treaxure.global.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.trip.treaxure.global.util.ImageUtils;

@ExtendWith(MockitoExtension.class)
class ImageSimilarityServiceTest {

    @Mock
    private ImageUtils imageUtils;

    @InjectMocks
    private LocalImageSimilarityService imageSimilarityService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(imageSimilarityService, "similarityThreshold", 0.75f);
    }

    @Test
    void testCompare_ShouldReturnSimilarityScore() throws IOException {
        // Given
        String referenceUrl = "https://example.com/reference.jpg";
        String targetUrl = "https://example.com/target.jpg";
        
        byte[] referenceBytes = "reference image data".getBytes();
        byte[] targetBytes = "target image data".getBytes();
        
        BufferedImage referenceImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage targetImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        
        when(imageUtils.loadImageFromUrl(referenceUrl)).thenReturn(referenceBytes);
        when(imageUtils.loadImageFromUrl(targetUrl)).thenReturn(targetBytes);
        when(imageUtils.byteArrayToBufferedImage(referenceBytes)).thenReturn(referenceImage);
        when(imageUtils.byteArrayToBufferedImage(targetBytes)).thenReturn(targetImage);
        
        // When
        float similarity = imageSimilarityService.compare(referenceUrl, targetUrl);
        
        // Then
        assertTrue(similarity >= 0 && similarity <= 1, "Similarity score should be between 0 and 1");
        verify(imageUtils, times(1)).loadImageFromUrl(referenceUrl);
        verify(imageUtils, times(1)).loadImageFromUrl(targetUrl);
        verify(imageUtils, times(1)).byteArrayToBufferedImage(referenceBytes);
        verify(imageUtils, times(1)).byteArrayToBufferedImage(targetBytes);
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
} 