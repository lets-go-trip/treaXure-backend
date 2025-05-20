# AWS 클라우드 서비스 통합 구현 요약

## 구현된 기능

### 1. S3 Presigned URL 발급 (POST /api/presigned-upload)
- 프론트엔드에서 S3에 직접 업로드할 수 있는 임시 URL 생성
- 사용자별/타임스탬프별 고유 경로 생성으로 충돌 방지
- 썸네일 객체 키 자동 생성

### 2. CloudFront 서명된 URL 발급 (POST /api/signed-url)
- CloudFront를 통한 서명된 URL 제공
- 사용자가 특정 이미지 자원에 안전하게 접근 가능
- 만료 시간 설정으로 액세스 제한

### 3. CloudFront 서명된 쿠키 발급 (GET /api/auth/cloudfront)
- 서명된 쿠키를 통해 다수의 CloudFront 리소스에 접근 가능
- 지정된 도메인 기반 쿠키 발급으로 보안 강화
- 만료 시간 자동 설정

### 4. CloudWatch 로깅
- 업로드/접근 이벤트 추적
- 사용자별, 파일 유형별 메트릭 수집
- 모니터링 및 추적 용이

### 5. SQS 메시지 전송
- 비동기 처리를 위한 SQS 메시지 큐 통합
- Lambda 함수 트리거 지원
- 썸네일 자동 생성 등 이미지 처리 자동화

## 구현 구조

```
com.trip.treaxure
  └── config
      ├── AppConfig.java             - 기본 설정 (ObjectMapper 등)
      ├── AwsConfig.java             - AWS 클라이언트 빈 설정
      └── WebConfig.java             - CORS 설정
  └── global
      ├── controller
      │   └── AwsController.java     - API 엔드포인트 구현
      ├── dto
      │   ├── PresignedUrlDto.java   - Presigned URL 응답 DTO
      │   ├── PresignedUrlRequest.java - Presigned URL 요청 DTO
      │   └── SignedUrlRequest.java  - 서명 URL 요청 DTO
      └── service
          ├── CloudFrontService.java - CloudFront 서비스
          ├── CloudWatchService.java - CloudWatch 로깅 서비스
          ├── S3Service.java         - S3 URL 생성 서비스
          └── SqsService.java        - SQS 메시지 서비스
```

## AWS 리소스 구성

![AWS 아키텍처](https://placeholder-for-aws-architecture-diagram.com)

1. **S3 버킷**: 이미지 원본 및 썸네일 저장
2. **CloudFront**: 이미지 배포 및 엣지 캐싱
3. **Lambda**: S3 업로드 이벤트 트리거 처리 (썸네일 자동 생성)
4. **SQS**: 비동기 메시지 처리
5. **CloudWatch**: 로깅 및 모니터링

## 보안 고려사항

1. **인증 통합**: Spring Security와 연동하여 사용자 권한 검증
2. **서명 만료**: 모든 서명된 URL/쿠키에 만료 시간 설정
3. **CORS 정책**: 안전한 크로스 오리진 요청 처리
4. **쿠키 보안**: Secure, HttpOnly, SameSite 설정
5. **환경 변수**: AWS 자격 증명 및 설정을 환경 변수로 관리

## 프로덕션 배포 체크리스트

- [ ] AWS IAM 권한 검토 및 최소 권한 원칙 적용
- [ ] CloudFront 키 페어 생성 및 관리
- [ ] S3 버킷 CORS 설정 확인
- [ ] CloudWatch 알림 설정
- [ ] 로드 테스트 수행
- [ ] CDN 캐싱 전략 수립
- [ ] 이미지 처리 Lambda 함수 구현
- [ ] 에러 처리 및 재시도 전략 수립