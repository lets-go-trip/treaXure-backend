## 📌 treaXure: *어서와 서울은 처음이지?*

서울의 명소를 탐험하며, 미션을 수행하고 보물을 찾는 **탐험 기반 게임형 여행 앱**입니다.

### ■ 컨셉 키워드
- **탐험**
- **보물찾기**
- **서울**
- **사진 미션**
- **위치 기반**

### ■ 페르소나
서울을 처음 방문하거나, 친구들에게 **서울의 숨겨진 명소**와 **나만의 여행 스팟**을 소개하고 싶은 사용자

## 🧑‍🤝‍🧑 사용자 역할

### 1. 관리자
- 장소 및 미션 콘텐츠 기획/관리
- 보물찾기 프로그램 생성 및 유지

### 2. 유저 (참여자)
- 서울 내 명소 탐험
- 미션 수행을 통한 점수 획득
- 포토 미션 참여

## 🗂️ 주요 기능

### 필수 기능
- 회원가입/로그인
- 지도 기반 장소 탐색
- 위치 기반 방문 판별
- 미션 리스트 및 상세 확인
- 사진 업로드 및 점수 지급
- 장소 관리 (CRUD)
- 미션 등록 (CRUD)
- 미션 사진 수동 선정

### 추가 기능 (향후 구현)
- AI 유사도 판별 미션
- 주간 베스트 투표
- 리더보드 / 랭킹
- 댓글/좋아요
- 친구 초대 이벤트
- 자동 사진 선정 로직
- 사용자 신고 관리
- 통계 대시보드

## 📱 페이지 구성

### 유저 페이지
1. 🗺️ 지도 페이지 (홈 화면)
2. 📋 미션 리스트 페이지
3. 📸 미션 상세 페이지
4. 📁 업로드/미션 기록 페이지
5. ⭐ 주간 미션/사진 선정 페이지
6. 🧭 마이페이지

### 관리자 페이지
1. 🔧 관리자 대시보드
2. 📍 장소 관리 페이지
3. 🧩 미션 관리 페이지
4. 🏆 미션 사진 채택 페이지

### 공통 페이지
1. 🔐 로그인/회원가입
2. 📜 이용안내/가이드
3. 🚫 사진 신고/관리

## 🎯 미션 유형 및 점수 체계

### 기본 미션
- 장소 방문: 기본 점수 획득

### 사진 기반 미션
- 일반 업로드: 기본 점수
- 미션 사진 업로드: 추가 점수
- 랜덤 미션 사진 따라찍기: 높은 점수
- 포즈/구도 따라하기: AI 유사도 기반 점수

### 미션 선정 보너스
- 주간 베스트 미션 사진 3장 선정
  - 사용자 투표 또는 관리자 선정
  - 추가 점수 제공

## 🚀 향후 확장 계획
- 리더보드(순위표) 도입
- 시즌별 챌린지
- 친구 초대 보상
- 테마별 미션 (봄꽃 명소, 야경 명소 등)

# Treaxure Backend

## 프로젝트 소개
여행 관련 미션과 트레져 사냥을 제공하는 앱의 백엔드 서버입니다.

## 기술 스택
- Java 21
- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- H2 Database (개발용)
- AWS (S3, CloudFront, SQS, CloudWatch)
- Gradle

## 환경 설정

### 개발 환경 준비하기
1. Java 21 설치
2. 소스코드 다운로드
```
git clone https://github.com/your-org/treaxure-backend.git
cd treaxure-backend
```

### AWS 및 환경 변수 설정
이 프로젝트는 다음과 같은 AWS 서비스를 사용합니다:
- S3: 이미지 저장
- CloudFront: 이미지 전송 및 보안
- SQS: 비동기 작업 처리
- CloudWatch: 모니터링

프로젝트 루트에 `.env` 파일을 생성하고 다음 환경 변수를 설정하세요:

```
# AWS 자격 증명
AWS_REGION=ap-northeast-2
AWS_ACCESS_KEY=your-access-key
AWS_SECRET_KEY=your-secret-key

# S3 설정
S3_BUCKET_NAME=your-bucket-name

# CloudFront 설정
CLOUDFRONT_DOMAIN=your-cloudfront-domain
CLOUDFRONT_KEY_PAIR_ID=your-key-pair-id
CLOUDFRONT_PRIVATE_KEY_PATH=./your-private-key.pem

# SQS 설정
SQS_QUEUE_URL=your-sqs-queue-url

# JWT 설정
JWT_SECRET=your-jwt-secret-key
JWT_ACCESS_TOKEN_VALIDITY=3600
JWT_REFRESH_TOKEN_VALIDITY=86400

# OAuth 설정 (옵션)
KAKAO_CLIENT_ID=your-kakao-client-id
KAKAO_REDIRECT_URI=http://localhost:8081/api/auth/kakao/callback
KAKAO_TOKEN_URI=https://kauth.kakao.com/oauth/token
KAKAO_USER_INFO_URI=https://kapi.kakao.com/v2/user/me

NAVER_CLIENT_ID=your-naver-client-id
NAVER_CLIENT_SECRET=your-naver-client-secret
NAVER_REDIRECT_URI=http://localhost:8081/api/auth/naver/callback
NAVER_TOKEN_URI=https://nid.naver.com/oauth2.0/token
NAVER_USER_INFO_URI=https://openapi.naver.com/v1/nid/me
```

### 실행하기
```
./gradlew bootRun
```

또는

```
./gradlew build
java -jar build/libs/treaxure-0.0.1-SNAPSHOT.jar
```

기본적으로 애플리케이션은 8081 포트에서 실행됩니다.

## API 문서
API 문서는 Swagger UI를 통해 제공됩니다.
서버 실행 후 다음 URL에서 확인할 수 있습니다:
```
http://localhost:8081/swagger-ui/index.html
```

## AWS 통합 설명서
AWS 서비스 사용에 대한 자세한 내용은 [README_AWS_INTEGRATION.md](README_AWS_INTEGRATION.md) 문서를 참조하세요.