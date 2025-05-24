# 이미지 유사도 API 구현 가이드

이 문서는 OpenAI Vision API를 활용한 이미지 유사도 검사 API를 설명합니다. 이 API는 미션 이미지와 사용자가 업로드한 이미지 간의 유사도를 계산하여 게임의 정확성을 검증합니다.

## 개요

이미지 유사도 API는 다음과 같은 기능을 제공합니다:

1. 두 이미지 URL을 입력받아 유사도 점수(0~1)를 계산
2. 기존 Shell 스크립트와 동일한 기능을 RESTful API로 제공
3. OpenAI의 Vision 모델을 활용한 고품질 이미지 설명 및 비교

## API 엔드포인트

### POST /api/image-similarity/evaluate

두 이미지의 유사도를 평가합니다.

**요청 본문 (Request Body):**

```json
{
  "referenceImageUrl": "https://example.com/reference.jpg",
  "targetImageUrl": "https://example.com/target.jpg"
}
```

**응답 (Response):**

```json
{
  "success": true,
  "data": {
    "referenceImageUrl": "https://example.com/reference.jpg",
    "targetImageUrl": "https://example.com/target.jpg",
    "similarityScore": 0.8523
  },
  "message": null
}
```

## 구현 세부사항

1. **OpenAiVisionSimilarityService**:
   - OpenAI Vision API를 활용하여 이미지 유사도를 계산하는 서비스
   - `ImageSimilarityService` 인터페이스를 구현하여 다른 서비스와 교체 가능

2. **ImageUtils**:
   - 이미지 다운로드 및 변환 유틸리티
   - URL에서 이미지를 로드하고 Content-Type을 결정

3. **설정**:
   - `application.yml`에 OpenAI API 키 및 관련 설정 추가
   - `ServiceConfig`를 통해 기본 유사도 서비스로 OpenAI 서비스 지정

## 동작 방식

1. 두 이미지 URL을 받아 각각 바이트 배열로 로드
2. 바이트 배열을 Base64로 인코딩
3. OpenAI API를 통해 각 이미지의 상세 설명을 추출
4. 두 이미지 설명을 다시 OpenAI API에 전달하여 유사도 점수(0~1) 계산
5. 결과를 클라이언트에 반환

## 환경 변수

```
OPENAI_API_KEY=your-openai-api-key
```

## 통합 방법

이 API는 다음과 같은 방식으로 기존 미션/게시물 시스템과 통합됩니다:

1. 사용자가 게시물 생성 시 자동으로 유사도 평가 실행
2. 특정 게시물에 대해 `/api/boards/{boardId}/evaluate` 엔드포인트로 수동 평가 가능
3. 독립적인 `/api/image-similarity/evaluate` 엔드포인트로 테스트 및 디버깅 가능

## 에러 처리

- 이미지 로딩 실패: 적절한 에러 메시지와 함께 RuntimeException 발생
- API 요청 실패: OpenAI API에서 반환된 에러 메시지 포함하여 예외 발생
- 유효하지 않은 유사도 점수: API가 숫자를 반환하지 않을 경우 예외 처리 