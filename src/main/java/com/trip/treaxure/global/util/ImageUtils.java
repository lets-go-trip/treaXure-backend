package com.trip.treaxure.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 이미지 처리를 위한 유틸리티 클래스
 */
@Slf4j
@Component
public class ImageUtils {

    private static final Map<String, String> EXTENSION_TO_CONTENT_TYPE = new HashMap<>();
    
    static {
        EXTENSION_TO_CONTENT_TYPE.put("jpg", "image/jpeg");
        EXTENSION_TO_CONTENT_TYPE.put("jpeg", "image/jpeg");
        EXTENSION_TO_CONTENT_TYPE.put("png", "image/png");
        EXTENSION_TO_CONTENT_TYPE.put("gif", "image/gif");
        EXTENSION_TO_CONTENT_TYPE.put("webp", "image/webp");
        EXTENSION_TO_CONTENT_TYPE.put("bmp", "image/bmp");
        EXTENSION_TO_CONTENT_TYPE.put("svg", "image/svg+xml");
    }

    /**
     * URL에서 이미지를 바이트 배열로 로드합니다.
     *
     * @param imageUrl 이미지 URL
     * @return 이미지 바이트 배열
     * @throws IOException URL에서 이미지를 로드하는 중 오류 발생 시
     */
    public byte[] loadImageFromUrl(String imageUrl) throws IOException {
        log.debug("Loading image from URL: {}", imageUrl);
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        try (InputStream inputStream = connection.getInputStream()) {
            return StreamUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            log.error("Error loading image from URL: {}", imageUrl, e);
            throw e;
        }
    }

    /**
     * 이미지 URL에서 Content-Type을 추출합니다.
     * 
     * @param imageUrl 이미지 URL
     * @return Content-Type 문자열
     */
    public String getContentType(String imageUrl) {
        try {
            // 먼저 URL 연결을 통해 Content-Type 확인 시도
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3000); // 짧은 타임아웃 설정
            String contentType = connection.getContentType();
            
            if (contentType != null && contentType.startsWith("image/")) {
                return contentType;
            }
            
            // URL 파일 확장자에서 추출
            String extension = getFileExtension(imageUrl).toLowerCase();
            return EXTENSION_TO_CONTENT_TYPE.getOrDefault(extension, "image/jpeg");
            
        } catch (Exception e) {
            log.warn("Error determining content type for URL: {}, using default", imageUrl, e);
            return "image/jpeg"; // 기본값
        }
    }
    
    /**
     * URL에서 파일 확장자를 추출합니다.
     * 
     * @param url 파일 URL
     * @return 파일 확장자
     */
    private String getFileExtension(String url) {
        String filename = url.substring(url.lastIndexOf('/') + 1);
        int dotIndex = filename.lastIndexOf('.');
        
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        
        return "jpg"; // 기본 확장자
    }

    /**
     * 바이트 배열에서 BufferedImage로 변환합니다.
     *
     * @param imageBytes 이미지 바이트 배열
     * @return BufferedImage 객체
     * @throws IOException 이미지 변환 중 오류 발생 시
     */
    public BufferedImage byteArrayToBufferedImage(byte[] imageBytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bis);
        } catch (IOException e) {
            log.error("Error converting byte array to BufferedImage", e);
            throw e;
        }
    }

    /**
     * BufferedImage를 바이트 배열로 변환합니다.
     *
     * @param image BufferedImage 객체
     * @param format 이미지 포맷 (예: "png", "jpg")
     * @return 이미지 바이트 배열
     * @throws IOException 이미지 변환 중 오류 발생 시
     */
    public byte[] bufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error converting BufferedImage to byte array", e);
            throw e;
        }
    }
} 