# AWS 통합 기능 구현 가이드

## 개요
이 문서는 프론트엔드에서 S3로 파일을 직접 업로드하고, CloudFront를 통해 안전하게 콘텐츠에 접근하기 위한 백엔드 API 구현에 대한 정보를 제공합니다.

## 구현된 기능
1. S3 Presigned URL을 통한 클라이언트 직접 업로드
2. CloudFront 서명된 URL 발급
3. CloudFront 서명된 쿠키 발급
4. Lambda 트리거를 위한 SQS 메시지 전송
5. CloudWatch를 통한 이벤트 로깅

## API 엔드포인트

### 1. Presigned URL 발급 API
- **URL**: `/api/presigned-upload`
- **Method**: `POST`
- **Request**:
```json
{
  "userNickname": "user123",
  "fileName": "image.jpg",
  "contentType": "image/jpeg"
}
```
- **Response**:
```json
{
  "presignedUrl": "https://s3.ap-northeast-2.amazonaws.com/bucket-name/...",
  "originalObjectKey": "images/user123/1625039876-abcd1234.jpg",
  "thumbnailObjectKey": "images/user123/1625039876-abcd1234_thumbnail.jpg",
  "bucketName": "your-bucket-name"
}
```

### 2. 서명된 URL 발급 API
- **URL**: `/api/signed-url`
- **Method**: `POST`
- **Request**:
```json
{
  "objectKey": "images/user123/1625039876-abcd1234.jpg"
}
```
- **Response**:
```json
{
  "signedUrl": "https://your-cloudfront-domain.cloudfront.net/..."
}
```

### 3. CloudFront 서명된 쿠키 발급 API
- **URL**: `/api/auth/cloudfront`
- **Method**: `GET`
- **Response**: 서명된 쿠키가 포함된 HTTP 응답
```json
{
  "message": "CloudFront 인증 완료"
}
```

## AWS 설정 방법

### 1. S3 설정
1. S3 버킷 생성
2. CORS 설정 추가 (클라이언트 직접 업로드 허용)
```xml
<CORSConfiguration>
  <CORSRule>
    <AllowedOrigin>http://localhost:3000</AllowedOrigin>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedHeader>*</AllowedHeader>
  </CORSRule>
</CORSConfiguration>
```

### 2. CloudFront 설정
1. CloudFront 배포 생성 (S3 버킷 연결)
2. 서명된 URL/쿠키 사용 설정
3. 키 페어 생성 또는 기존 키 페어 사용
4. 키 페어 ID 및 개인 키 저장

### 3. Lambda 설정 (이미지 처리용)
1. Lambda 함수 생성 (썸네일 생성 로직 구현)
2. S3 트리거 설정 (객체 생성 이벤트)
3. SQS 연동 (필요한 경우)

### 4. SQS 설정
1. SQS 대기열 생성
2. Lambda 함수에 권한 부여 (필요한 경우)

### 5. CloudWatch 설정
1. 필요한 경우 CloudWatch 경보 및 대시보드 구성

## 환경 변수 설정
애플리케이션 실행을 위해 다음 환경 변수를 설정하세요:

```
AWS_S3_BUCKET_NAME=your-s3-bucket-name
AWS_CLOUDFRONT_DOMAIN=your-cloudfront-domain
AWS_CLOUDFRONT_KEY_PAIR_ID=your-key-pair-id
AWS_CLOUDFRONT_PRIVATE_KEY_PATH=./private_key.pem
AWS_SQS_QUEUE_URL=your-sqs-queue-url
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
```

## 인증 관련 참고사항
이 예제에서는 단순화를 위해 인증 정보를 생략했습니다. 실제 구현 시 Spring Security를 활용하여 사용자 인증 후 해당 사용자의 닉네임이나 ID를 활용하는 것이 좋습니다.

## 프론트엔드 연동 예제 (React)

```javascript
// S3 직접 업로드 예제
async function uploadToS3(file) {
  // 1. Presigned URL 요청
  const response = await fetch('http://your-api/api/presigned-upload', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      userNickname: 'user123',
      fileName: file.name,
      contentType: file.type
    })
  });
  
  const data = await response.json();
  
  // 2. Presigned URL을 통해 S3에 직접 업로드
  await fetch(data.presignedUrl, {
    method: 'PUT',
    headers: { 'Content-Type': file.type },
    body: file
  });
  
  return {
    originalUrl: `https://your-cloudfront-domain/${data.originalObjectKey}`,
    thumbnailUrl: `https://your-cloudfront-domain/${data.thumbnailObjectKey}`
  };
}

// CloudFront 인증 쿠키 획득
async function getCloudfrontCookies() {
  await fetch('http://your-api/api/auth/cloudfront', {
    method: 'GET',
    credentials: 'include'  // 쿠키 포함
  });
  // 쿠키는 브라우저에 자동 저장됨
}
```

## 트러블슈팅
- CORS 오류: S3 버킷 및 API 서버의 CORS 설정 확인
- 권한 오류: IAM 권한 및 정책 확인
- 키 페어 문제: 키 페어 ID와 개인 키 일치 여부 확인
- Lambda 실행 오류: CloudWatch 로그 확인 