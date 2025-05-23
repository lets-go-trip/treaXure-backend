package com.trip.treaxure.global.util;

import com.trip.treaxure.global.service.LocalImageSimilarityService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * 이미지 유사도 테스트를 위한 간단한 명령줄 도구
 * 이 클래스는 테스트 용도로만 사용됩니다.
 */
public class ImageSimilarityTestTool {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 이미지 유사도 테스트 도구 ===");
        System.out.println("두 이미지의 URL 또는 파일 경로를 입력하세요.");
        
        Scanner scanner = new Scanner(System.in);
        
        // 첫 번째 이미지 입력
        System.out.print("첫 번째 이미지 URL 또는 경로: ");
        String image1Path = scanner.nextLine().trim();
        
        // 두 번째 이미지 입력
        System.out.print("두 번째 이미지 URL 또는 경로: ");
        String image2Path = scanner.nextLine().trim();
        
        // 이미지 로드
        BufferedImage image1 = loadImage(image1Path);
        BufferedImage image2 = loadImage(image2Path);
        
        if (image1 == null || image2 == null) {
            System.out.println("이미지를 로드할 수 없습니다.");
            return;
        }
        
        System.out.println("이미지 로드 완료.");
        System.out.println("첫 번째 이미지 크기: " + image1.getWidth() + "x" + image1.getHeight());
        System.out.println("두 번째 이미지 크기: " + image2.getWidth() + "x" + image2.getHeight());
        
        // 이미지 유사도 계산
        TestLocalImageSimilarityService service = new TestLocalImageSimilarityService();
        float averageHashSimilarity = service.compareWithAverageHash(image1, image2);
        float perceptualHashSimilarity = service.compareWithPerceptualHash(image1, image2);
        float weightedSimilarity = (perceptualHashSimilarity * 0.7f) + (averageHashSimilarity * 0.3f);
        
        // 결과 출력
        System.out.println("\n=== 유사도 계산 결과 ===");
        System.out.println("Average Hash 유사도: " + formatPercentage(averageHashSimilarity));
        System.out.println("Perceptual Hash 유사도: " + formatPercentage(perceptualHashSimilarity));
        System.out.println("가중 평균 유사도: " + formatPercentage(weightedSimilarity));
        
        String similarityDescription = getSimilarityDescription(weightedSimilarity);
        System.out.println("결론: " + similarityDescription);
    }
    
    private static BufferedImage loadImage(String path) {
        try {
            if (path.startsWith("http://") || path.startsWith("https://")) {
                // URL에서 이미지 로드
                URL url = new URL(path);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                return ImageIO.read(connection.getInputStream());
            } else {
                // 로컬 파일에서 이미지 로드
                return ImageIO.read(new File(path));
            }
        } catch (IOException e) {
            System.err.println("이미지 로드 실패: " + e.getMessage());
            return null;
        }
    }
    
    private static String formatPercentage(float value) {
        return String.format("%.2f%%", value * 100);
    }
    
    private static String getSimilarityDescription(float similarity) {
        if (similarity > 0.95) {
            return "거의 동일한 이미지 (매우 높은 유사도)";
        } else if (similarity > 0.85) {
            return "매우 유사한 이미지 (높은 유사도)";
        } else if (similarity > 0.75) {
            return "유사한 이미지 (중간 유사도)";
        } else if (similarity > 0.6) {
            return "약간 유사한 이미지 (낮은 유사도)";
        } else {
            return "다른 이미지 (유사하지 않음)";
        }
    }
    
    /**
     * 테스트 용도의 이미지 유사도 서비스 구현체
     */
    private static class TestLocalImageSimilarityService {
        
        private static final int RESIZE_WIDTH = 32;
        private static final int RESIZE_HEIGHT = 32;
        
        /**
         * Average Hash 알고리즘으로 이미지 유사도 계산
         */
        public float compareWithAverageHash(BufferedImage img1, BufferedImage img2) {
            BufferedImage resized1 = resizeAndGrayscale(img1, 8, 8);
            BufferedImage resized2 = resizeAndGrayscale(img2, 8, 8);
            
            int avg1 = calculateAverage(resized1);
            int avg2 = calculateAverage(resized2);
            
            long hash1 = createHash(resized1, avg1);
            long hash2 = createHash(resized2, avg2);
            
            int hammingDistance = Long.bitCount(hash1 ^ hash2);
            
            return 1.0f - (hammingDistance / 64.0f);
        }
        
        /**
         * Perceptual Hash 알고리즘으로 이미지 유사도 계산
         */
        public float compareWithPerceptualHash(BufferedImage img1, BufferedImage img2) {
            BufferedImage resized1 = resizeAndGrayscale(img1, RESIZE_WIDTH, RESIZE_HEIGHT);
            BufferedImage resized2 = resizeAndGrayscale(img2, RESIZE_WIDTH, RESIZE_HEIGHT);
            
            double[] features1 = extractFeatures(resized1);
            double[] features2 = extractFeatures(resized2);
            
            return calculateCosineSimilarity(features1, features2);
        }
        
        private BufferedImage resizeAndGrayscale(BufferedImage original, int width, int height) {
            BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            resized.getGraphics().drawImage(original, 0, 0, width, height, null);
            return resized;
        }
        
        private int calculateAverage(BufferedImage img) {
            int width = img.getWidth();
            int height = img.getHeight();
            int sum = 0;
            int count = 0;
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = img.getRGB(x, y);
                    int gray = rgb & 0xFF;
                    sum += gray;
                    count++;
                }
            }
            
            return (count > 0) ? sum / count : 0;
        }
        
        private long createHash(BufferedImage img, int avgValue) {
            int width = img.getWidth();
            int height = img.getHeight();
            long hash = 0;
            int bitPosition = 0;
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = img.getRGB(x, y);
                    int gray = rgb & 0xFF;
                    
                    if (gray >= avgValue) {
                        hash |= (1L << bitPosition);
                    }
                    
                    bitPosition++;
                    if (bitPosition >= 64) break;
                }
                if (bitPosition >= 64) break;
            }
            
            return hash;
        }
        
        private double[] extractFeatures(BufferedImage img) {
            int width = img.getWidth();
            int height = img.getHeight();
            double[] features = new double[width * height];
            int index = 0;
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = img.getRGB(x, y);
                    int gray = rgb & 0xFF;
                    features[index++] = gray / 255.0;
                }
            }
            
            return features;
        }
        
        private float calculateCosineSimilarity(double[] vector1, double[] vector2) {
            double dotProduct = 0.0;
            double norm1 = 0.0;
            double norm2 = 0.0;
            
            for (int i = 0; i < vector1.length; i++) {
                dotProduct += vector1[i] * vector2[i];
                norm1 += Math.pow(vector1[i], 2);
                norm2 += Math.pow(vector2[i], 2);
            }
            
            if (norm1 == 0 || norm2 == 0) {
                return 0;
            }
            
            double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
            
            return (float) ((similarity + 1) / 2);
        }
    }
} 