package com.trip.treaxure.global.service;

import com.trip.treaxure.global.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 로컬 구현 기반의 이미지 유사도 계산 서비스
 * 자바 표준 라이브러리와 commons-math를 사용한 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocalImageSimilarityService implements ImageSimilarityService {

    private final ImageUtils imageUtils;
    
    @Value("${image.similarity.threshold:0.75}")
    private float similarityThreshold;
    
    // 이미지 리사이징 크기 - 성능과 정확도의 균형점
    private static final int RESIZE_WIDTH = 32;
    private static final int RESIZE_HEIGHT = 32;

    /**
     * 두 이미지의 유사도를 계산합니다.
     * perceptual hash와 유사한 알고리즘을 사용합니다.
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
            
            BufferedImage referenceImage = imageUtils.byteArrayToBufferedImage(referenceImageBytes);
            BufferedImage targetImage = imageUtils.byteArrayToBufferedImage(targetImageBytes);
            
            // 이미지 특성 추출 및 유사도 계산
            float averageHashSimilarity = compareWithAverageHash(referenceImage, targetImage);
            float perceptualHashSimilarity = compareWithPerceptualHash(referenceImage, targetImage);
            
            // 가중 평균 계산 (perceptualHash에 더 높은 가중치)
            float weightedSimilarity = (perceptualHashSimilarity * 0.7f) + (averageHashSimilarity * 0.3f);
            
            log.info("Image similarity between {} and {}: {} (pHash: {}, aHash: {})", 
                    referenceImageUrl, targetImageUrl, weightedSimilarity, 
                    perceptualHashSimilarity, averageHashSimilarity);
            
            return weightedSimilarity;
            
        } catch (IOException e) {
            log.error("Error comparing images: {} and {}", referenceImageUrl, targetImageUrl, e);
            throw new RuntimeException("이미지 유사도 계산 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Average Hash 알고리즘으로 이미지 유사도 계산
     * 1. 이미지를 그레이스케일로 변환
     * 2. 작은 크기로 리사이징 (세부 정보 제거)
     * 3. 각 픽셀값이 평균값보다 크면 1, 작으면 0인 해시값 생성
     * 4. 두 해시의 해밍 거리 계산
     */
    private float compareWithAverageHash(BufferedImage img1, BufferedImage img2) {
        // 리사이징 및 그레이스케일 변환
        BufferedImage resized1 = resizeAndGrayscale(img1, 8, 8);
        BufferedImage resized2 = resizeAndGrayscale(img2, 8, 8);
        
        // 평균값 계산
        int avg1 = calculateAverage(resized1);
        int avg2 = calculateAverage(resized2);
        
        // 해시 생성
        long hash1 = createHash(resized1, avg1);
        long hash2 = createHash(resized2, avg2);
        
        // 해밍 거리 계산 (다른 비트 수)
        int hammingDistance = Long.bitCount(hash1 ^ hash2);
        
        // 정규화된 유사도 반환 (0~1)
        return 1.0f - (hammingDistance / 64.0f);
    }
    
    /**
     * Perceptual Hash와 유사한 방식으로 유사도 계산
     * 이미지를 더 큰 크기로 리사이징하고 특징 벡터 추출
     */
    private float compareWithPerceptualHash(BufferedImage img1, BufferedImage img2) {
        // 리사이징 및 그레이스케일 변환
        BufferedImage resized1 = resizeAndGrayscale(img1, RESIZE_WIDTH, RESIZE_HEIGHT);
        BufferedImage resized2 = resizeAndGrayscale(img2, RESIZE_WIDTH, RESIZE_HEIGHT);
        
        // 특징 벡터 추출
        double[] features1 = extractFeatures(resized1);
        double[] features2 = extractFeatures(resized2);
        
        // 코사인 유사도 계산
        return calculateCosineSimilarity(features1, features2);
    }
    
    /**
     * 이미지를 리사이징하고 그레이스케일로 변환
     */
    private BufferedImage resizeAndGrayscale(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }
    
    /**
     * 그레이스케일 이미지의 픽셀 평균값 계산
     */
    private int calculateAverage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int sum = 0;
        int count = 0;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int gray = rgb & 0xFF; // 그레이스케일 값
                sum += gray;
                count++;
            }
        }
        
        return (count > 0) ? sum / count : 0;
    }
    
    /**
     * 평균값을 기준으로 해시 생성
     */
    private long createHash(BufferedImage img, int avgValue) {
        int width = img.getWidth();
        int height = img.getHeight();
        long hash = 0;
        int bitPosition = 0;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int gray = rgb & 0xFF;
                
                // 평균보다 크면 1, 작으면 0
                if (gray >= avgValue) {
                    hash |= (1L << bitPosition);
                }
                
                bitPosition++;
                if (bitPosition >= 64) break; // 최대 64비트
            }
            if (bitPosition >= 64) break;
        }
        
        return hash;
    }
    
    /**
     * 이미지에서 특징 벡터 추출
     */
    private double[] extractFeatures(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        double[] features = new double[width * height];
        int index = 0;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int gray = rgb & 0xFF;
                features[index++] = gray / 255.0; // 0~1 사이 값으로 정규화
            }
        }
        
        return features;
    }
    
    /**
     * 두 벡터 간의 코사인 유사도 계산
     */
    private float calculateCosineSimilarity(double[] vector1, double[] vector2) {
        RealVector v1 = new ArrayRealVector(vector1);
        RealVector v2 = new ArrayRealVector(vector2);
        
        double dotProduct = v1.dotProduct(v2);
        double norm1 = v1.getNorm();
        double norm2 = v2.getNorm();
        
        // 0으로 나누는 것을 방지
        if (norm1 == 0 || norm2 == 0) {
            return 0;
        }
        
        double similarity = dotProduct / (norm1 * norm2);
        
        // 결과를 0~1 사이로 정규화 (코사인 유사도는 -1~1 사이의 값)
        return (float) ((similarity + 1) / 2);
    }
} 