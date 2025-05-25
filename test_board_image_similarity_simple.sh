#!/bin/bash

# 서버 URL 설정
SERVER_URL="http://localhost:8081"

# 테스트할 이미지 URL
REFERENCE_URL="https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg"
TARGET_URL="https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp"

echo "이미지 유사도 API 테스트"
echo "참조 이미지: $REFERENCE_URL"
echo "대상 이미지: $TARGET_URL"
echo ""

# 먼저 게시물이 있는지 확인
echo "1. 게시물 목록 조회"
BOARDS_RESPONSE=$(curl -s -X GET "$SERVER_URL/api/boards")
echo "$BOARDS_RESPONSE" | jq . 2>/dev/null || echo "$BOARDS_RESPONSE"
echo ""

# 게시물이 있으면 첫 번째 게시물의 ID를 가져옴
if command -v jq &> /dev/null; then
    if [[ $(echo $BOARDS_RESPONSE | jq '.data | length') -gt 0 ]]; then
        BOARD_ID=$(echo $BOARDS_RESPONSE | jq -r '.data[0].boardId')
        echo "기존 게시물 ID: $BOARD_ID"
        
        # 이미지 유사도 평가
        echo "2. 이미지 유사도 평가"
        SIMILARITY_RESPONSE=$(curl -s -X POST "$SERVER_URL/api/boards/$BOARD_ID/evaluate")
        echo "$SIMILARITY_RESPONSE" | jq . 2>/dev/null || echo "$SIMILARITY_RESPONSE"
    else
        echo "게시물이 없습니다. 테스트를 종료합니다."
    fi
else
    echo "jq가 설치되어 있지 않아 게시물 ID를 추출할 수 없습니다."
fi 