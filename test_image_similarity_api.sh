#!/bin/bash

# 서버 URL 설정
SERVER_URL="http://localhost:8081"

# 테스트할 이미지 URL
REFERENCE_URL="https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg"
TARGET_URL="https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp"

# 이미지 유사도 API 테스트
echo "이미지 유사도 API 테스트 중..."
echo "기준 이미지: $REFERENCE_URL"
echo "대상 이미지: $TARGET_URL"
echo ""

# API 호출 및 결과 저장
RESPONSE=$(curl -s -X POST "$SERVER_URL/api/image-similarity/evaluate" \
    -H "Content-Type: application/json" \
    -d "{
        \"referenceImageUrl\": \"$REFERENCE_URL\",
        \"targetImageUrl\": \"$TARGET_URL\"
    }")

# 응답 파싱 (jq가 없으면 Python 사용)
if command -v jq &> /dev/null; then
    SUCCESS=$(echo $RESPONSE | jq -r '.success')
    SCORE=$(echo $RESPONSE | jq -r '.data.similarityScore')
else
    # Python을 사용한 파싱 대체
    SUCCESS=$(python3 -c "import json, sys; print(json.loads('$RESPONSE')['success'])")
    SCORE=$(python3 -c "import json, sys; print(json.loads('$RESPONSE')['data']['similarityScore'])")
fi

# 결과 출력
echo "API 응답:"
echo "$RESPONSE" | jq . 2>/dev/null || echo "$RESPONSE"
echo ""

# 성공 여부 확인
if [ "$SUCCESS" = "true" ]; then
    echo "테스트 성공! 유사도 점수: $SCORE"
    exit 0
else
    echo "테스트 실패!"
    exit 1
fi 