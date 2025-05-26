#!/bin/bash

# 서버 URL 설정
SERVER_URL="http://localhost:8081"

echo "1. 게시물 생성 테스트 (자동 유사도 평가 포함)"
BOARD_RESPONSE=$(curl -s -X 'POST' \
  "$SERVER_URL/api/boards" \
  -H 'Content-Type: application/json' \
  -d '{
    "missionId": 1,
    "imageUrl": "https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp",
    "title": "북촌 한옥마을 방문"
  }')

echo "게시물 생성 응답:"
echo "$BOARD_RESPONSE" | jq . 2>/dev/null || echo "$BOARD_RESPONSE"
echo ""

# 게시물 ID 추출
if command -v jq &> /dev/null; then
    BOARD_ID=$(echo $BOARD_RESPONSE | jq -r '.data.boardId')
else
    # Python을 사용한 파싱 대체
    BOARD_ID=$(python3 -c "import json, sys; print(json.loads('$BOARD_RESPONSE')['data']['boardId'])")
fi

echo "생성된 게시물 ID: $BOARD_ID"
echo ""

echo "2. 수동 이미지 유사도 평가 테스트"
SIMILARITY_RESPONSE=$(curl -s -X 'POST' \
  "$SERVER_URL/api/boards/$BOARD_ID/evaluate")

echo "유사도 평가 응답:"
echo "$SIMILARITY_RESPONSE" | jq . 2>/dev/null || echo "$SIMILARITY_RESPONSE"
echo ""

# 유사도 점수 추출
if command -v jq &> /dev/null; then
    SIMILARITY_SCORE=$(echo $SIMILARITY_RESPONSE | jq -r '.data')
else
    # Python을 사용한 파싱 대체
    SIMILARITY_SCORE=$(python3 -c "import json, sys; print(json.loads('$SIMILARITY_RESPONSE')['data'])")
fi

echo "이미지 유사도 점수: $SIMILARITY_SCORE"
echo ""

echo "테스트 완료!" 