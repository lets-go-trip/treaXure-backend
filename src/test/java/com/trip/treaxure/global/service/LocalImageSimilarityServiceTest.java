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

import javax.imageio.ImageIO;

@ExtendWith(MockitoExtension.class)
class LocalImageSimilarityServiceTest {

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
        
        // Create simple test images
        BufferedImage referenceImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        BufferedImage targetImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        
        // Add some pattern to the reference image
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                if ((x + y) % 10 == 0) {
                    referenceImage.setRGB(x, y, 0xFFFFFF); // White
                } else {
                    referenceImage.setRGB(x, y, 0x000000); // Black
                }
            }
        }
        
        // Add similar pattern to the target image with slight variations
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                if ((x + y + 2) % 10 == 0) {
                    targetImage.setRGB(x, y, 0xFFFFFF); // White
                } else {
                    targetImage.setRGB(x, y, 0x000000); // Black
                }
            }
        }
        
        byte[] referenceBytes = new byte[1000]; // Dummy bytes
        byte[] targetBytes = new byte[1000]; // Dummy bytes
        
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
    }
    
    @Test
    void testCompare_WithIdenticalImages_ShouldReturnHighSimilarity() throws IOException {
        // Given
        String referenceUrl = "https://example.com/reference.jpg";
        String targetUrl = "https://example.com/same-image.jpg";
        
        // Create a test image
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                image.setRGB(x, y, (x * y) % 255 << 16 | x % 255 << 8 | y % 255);
            }
        }
        
        byte[] imageBytes = new byte[1000]; // Dummy bytes
        
        when(imageUtils.loadImageFromUrl(anyString())).thenReturn(imageBytes);
        when(imageUtils.byteArrayToBufferedImage(any())).thenReturn(image);
        
        // When
        float similarity = imageSimilarityService.compare(referenceUrl, targetUrl);
        
        // Then
        assertEquals(1.0f, similarity, 0.01f, "Identical images should have similarity close to 1.0");
    }
    
    @Test
    void testCompare_WhenImageLoadingFails_ShouldThrowException() throws IOException {
        // Given
        String referenceUrl = "https://example.com/reference.jpg";
        String targetUrl = "https://example.com/target.jpg";
        
        when(imageUtils.loadImageFromUrl(referenceUrl)).thenThrow(new IOException("Failed to load image"));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            imageSimilarityService.compare(referenceUrl, targetUrl);
        });
    }
} 