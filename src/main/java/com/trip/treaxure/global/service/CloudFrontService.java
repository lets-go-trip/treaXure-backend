package com.trip.treaxure.global.service;

import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudFrontService {

    private final AmazonCloudFrontClient cloudFrontClient;
    
    @Value("${aws.cloudfront.domain-name}")
    private String cloudFrontDomain;

    @Value("${aws.cloudfront.key-pair-id}")
    private String cloudFrontKeyPairId;
    
    @Value("${aws.cloudfront.private-key-path}")
    private String privateKeyPath;

    @Getter
    @Value("${aws.cloudfront.expiration-time-seconds}")
    private long expirationTimeSeconds;

    /**
     * CloudFront를 통해 서명된 URL을 생성
     * @param s3ObjectKey S3 객체 키 (경로)
     * @return 서명된 CloudFront URL
     */
    public String generateSignedUrl(String s3ObjectKey) {
        try {
            log.debug("CloudFront 서명 URL 생성 시작: 도메인={}, 경로={}, 키ID={}", 
                      cloudFrontDomain, s3ObjectKey, cloudFrontKeyPairId);
            
            // 리소스 URL 구성
            String resourceUrl = String.format("https://%s/%s", cloudFrontDomain, s3ObjectKey);
            
            // 개인 키 파일 확인
            File keyFile = new File(privateKeyPath);
            if (!keyFile.exists()) {
                log.warn("프라이빗 키 파일을 찾을 수 없습니다: {}. 개발용 URL 생성", privateKeyPath);
                // 개발 환경용 더미 URL 반환
                return resourceUrl + "?dummy-signature=test&key-pair-id=" + cloudFrontKeyPairId;
            }
            
            Date expirationDate = new Date(System.currentTimeMillis() + (expirationTimeSeconds * 1000));
            
            // CloudFront 서명된 URL 생성 - SignerUtils.Protocol 사용
            String signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                SignerUtils.Protocol.https, 
                cloudFrontDomain,
                keyFile,
                s3ObjectKey,
                cloudFrontKeyPairId,
                expirationDate
            );
            
            if (signedUrl != null && signedUrl.startsWith("https://https://")) {
                signedUrl = signedUrl.replace("https://https://", "https://");
                log.debug("중복된 https:// 수정 완료");
            }
            log.debug("CloudFront 서명된 URL 생성 완료: {}", signedUrl);
            return signedUrl;
            
        } catch (Exception e) {
            log.error("CloudFront 서명된 URL 생성 실패: {}", e.getMessage(), e);
            // 실패 시 서명되지 않은 URL 반환 (개발 환경용)
            String fallbackUrl = String.format("https://%s/%s", cloudFrontDomain, s3ObjectKey);
            return fallbackUrl;
        }
    }

    /**
     * CloudFront 서명된 쿠키 헤더 생성
     * @return 서명된 쿠키를 포함한 HTTP 헤더 (Set-Cookie 형식)
     */
    public Map<String, String> generateSignedCookies() {
        try {
            log.debug("CloudFront 서명 쿠키 생성 시작: 도메인={}, 키ID={}", 
                      cloudFrontDomain, cloudFrontKeyPairId);
            
            // 와일드카드 리소스 패턴 - 모든 리소스 접근용
            String resourceUrlPattern = String.format("https://%s/*", cloudFrontDomain);
            
            // 개인 키 파일 확인
            File keyFile = new File(privateKeyPath);
            if (!keyFile.exists()) {
                log.warn("프라이빗 키 파일을 찾을 수 없습니다: {}. 개발용 더미 쿠키 생성", privateKeyPath);
                // 개발 환경용 더미 쿠키 생성
                return createDummyCookies();
            }
            
            Date expirationDate = new Date(System.currentTimeMillis() + (expirationTimeSeconds * 1000));
            
            // CloudFront 서명된 쿠키 생성
            CloudFrontCookieSigner.CookiesForCannedPolicy signedCookies = 
                CloudFrontCookieSigner.getCookiesForCannedPolicy(
                    SignerUtils.Protocol.https, 
                    cloudFrontDomain, 
                    keyFile, 
                    resourceUrlPattern, 
                    cloudFrontKeyPairId, 
                    expirationDate
                );
            
            // 쿠키 헤더 변환
            Map<String, String> cookieHeaders = new HashMap<>();
            
            // 키 페어 ID 쿠키 
            String keyPairIdName = signedCookies.getKeyPairId().getKey();
            String keyPairIdValue = signedCookies.getKeyPairId().getValue();
            ResponseCookie keyPairIdCookie = createCookie(keyPairIdName, keyPairIdValue);
            cookieHeaders.put(keyPairIdName, keyPairIdCookie.toString());
            
            // 서명 쿠키
            String signatureName = signedCookies.getSignature().getKey();
            String signatureValue = signedCookies.getSignature().getValue();
            ResponseCookie signatureCookie = createCookie(signatureName, signatureValue);
            cookieHeaders.put(signatureName, signatureCookie.toString());
            
            // 만료 쿠키
            String expiresName = signedCookies.getExpires().getKey();
            String expiresValue = signedCookies.getExpires().getValue();
            ResponseCookie expiresCookie = createCookie(expiresName, expiresValue);
            cookieHeaders.put(expiresName, expiresCookie.toString());
            
            log.debug("CloudFront 쿠키 생성 완료 - 쿠키 수: {}", cookieHeaders.size());
            return cookieHeaders;
        } catch (Exception e) {
            log.error("CloudFront 서명된 쿠키 생성 실패: {}", e.getMessage(), e);
            // 실패 시 개발용 더미 쿠키 반환
            return createDummyCookies();
        }
    }
    
    /**
     * 개발 환경용 더미 쿠키 생성
     * @return 더미 쿠키를 포함한 HTTP 헤더 맵
     */
    private Map<String, String> createDummyCookies() {
        Map<String, String> cookieHeaders = new HashMap<>();
        
        // 더미 키 페어 ID 쿠키
        ResponseCookie keyPairIdCookie = createCookie("CloudFront-Key-Pair-Id", cloudFrontKeyPairId);
        cookieHeaders.put("CloudFront-Key-Pair-Id", keyPairIdCookie.toString());
        
        // 더미 정책 쿠키
        ResponseCookie policyCookie = createCookie("CloudFront-Policy", "dummy-policy-for-development");
        cookieHeaders.put("CloudFront-Policy", policyCookie.toString());
        
        // 더미 서명 쿠키
        ResponseCookie signatureCookie = createCookie("CloudFront-Signature", "dummy-signature-for-development");
        cookieHeaders.put("CloudFront-Signature", signatureCookie.toString());
        
        log.debug("개발용 더미 쿠키 생성 완료 - 쿠키 수: {}", cookieHeaders.size());
        return cookieHeaders;
    }
    
    /**
     * ResponseCookie 객체 생성
     * @param name 쿠키 이름
     * @param value 쿠키 값
     * @return ResponseCookie 객체
     */
    private ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expirationTimeSeconds)
                .sameSite("None")
                .build();
    }
} 