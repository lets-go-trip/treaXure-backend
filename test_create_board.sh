#!/bin/bash

# 서버 URL 설정
SERVER_URL="http://localhost:8081"

echo "=== 게시물 생성 테스트 ==="

# 1. 미션 ID 3에 대한 게시물 생성
echo "게시물 생성 요청 중..."
RESPONSE=$(curl -s -X POST \
  "$SERVER_URL/api/boards" \
  -H "Content-Type: application/json" \
  -d '{
    "missionId": 3,
    "imageUrl": "https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp",
    "title": "북촌 한옥마을 방문 테스트"
  }')

echo "응답: $RESPONSE"

# 응답 확인
SUCCESS=$(python3 -c "
import json, sys
try:
    data = json.loads('$RESPONSE')
    print(str(data.get('success')).lower())
except Exception as e:
    print('false')
")

if [ "$SUCCESS" = "true" ]; then
    echo "게시물 생성 성공!"
    
    # 게시물 ID 추출
    BOARD_ID=$(python3 -c "
import json, sys
try:
    data = json.loads('$RESPONSE')
    print(data['data']['boardId'])
except Exception as e:
    print('error')
")
    
    echo "생성된 게시물 ID: $BOARD_ID"
    
    # 이미지 유사도 평가
    echo ""
    echo "이미지 유사도 평가 중..."
    SIMILARITY_RESPONSE=$(curl -s -X POST "$SERVER_URL/api/boards/$BOARD_ID/evaluate")
    echo "응답: $SIMILARITY_RESPONSE"
else
    echo "게시물 생성 실패!"
    
    # 오류 메시지 추출
    ERROR_MSG=$(python3 -c "
import json, sys
try:
    data = json.loads('$RESPONSE')
    print(data.get('message', '알 수 없는 오류'))
except Exception as e:
    print('응답 파싱 실패: ' + str(e))
")
    
    echo "오류 메시지: $ERROR_MSG"
fi 