#!/bin/bash

# 서버 URL 설정
SERVER_URL="http://localhost:8081"

echo "=== 이미지 유사도 평가 테스트 ==="
echo ""

# 1. 기존 게시물 목록 조회
echo "1. 기존 게시물 목록 조회"
BOARDS_RESPONSE=$(curl -s -X GET "$SERVER_URL/api/boards")

# Python을 사용하여 JSON 응답 처리
BOARDS_COUNT=$(python3 -c "
import json, sys
try:
    data = json.loads('$BOARDS_RESPONSE')
    if data.get('success') and data.get('data'):
        print(len(data['data']))
    else:
        print(0)
except:
    print(0)
")

echo "게시물 수: $BOARDS_COUNT"

if [ "$BOARDS_COUNT" -gt 0 ]; then
    # 첫 번째 게시물 ID 추출
    BOARD_ID=$(python3 -c "
    import json
    data = json.loads('$BOARDS_RESPONSE')
    print(data['data'][0]['boardId'])
    ")
    
    MISSION_ID=$(python3 -c "
    import json
    data = json.loads('$BOARDS_RESPONSE')
    print(data['data'][0]['missionId'])
    ")
    
    echo "테스트할 게시물 ID: $BOARD_ID, 미션 ID: $MISSION_ID"
    
    # 2. 기존 게시물에 대한 이미지 유사도 평가
    echo ""
    echo "2. 기존 게시물에 대한 이미지 유사도 평가"
    SIMILARITY_RESPONSE=$(curl -s -X POST "$SERVER_URL/api/boards/$BOARD_ID/evaluate")
    echo "응답: $SIMILARITY_RESPONSE"
    
    # 유사도 점수 추출
    SIMILARITY_SCORE=$(python3 -c "
    import json, sys
    try:
        data = json.loads('$SIMILARITY_RESPONSE')
        if data.get('success') and data.get('data') is not None:
            print(data['data'])
        else:
            print('평가 실패: ' + data.get('message', '알 수 없는 오류'))
    except Exception as e:
        print(f'파싱 오류: {e}')
    ")
    
    echo "이미지 유사도 점수: $SIMILARITY_SCORE"
else
    echo "게시물이 없습니다. 새 게시물을 생성합니다."
    
    # 3. 새 게시물 생성
    echo ""
    echo "3. 새 게시물 생성 (자동 유사도 평가 포함)"
    BOARD_RESPONSE=$(curl -s -X 'POST' \
      "$SERVER_URL/api/boards" \
      -H 'Content-Type: application/json' \
      -d '{
        "missionId": 3,
        "imageUrl": "https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp",
        "title": "북촌 한옥마을 방문 테스트"
      }')
    
    echo "게시물 생성 응답: $BOARD_RESPONSE"
    
    # 게시물 ID 추출
    BOARD_ID=$(python3 -c "
    import json, sys
    try:
        data = json.loads('$BOARD_RESPONSE')
        if data.get('success') and data.get('data'):
            print(data['data']['boardId'])
        else:
            print('생성 실패: ' + data.get('message', '알 수 없는 오류'))
    except Exception as e:
        print(f'파싱 오류: {e}')
    ")
    
    if [[ $BOARD_ID =~ ^[0-9]+$ ]]; then
        echo "생성된 게시물 ID: $BOARD_ID"
        
        # 4. 수동 이미지 유사도 평가
        echo ""
        echo "4. 수동 이미지 유사도 평가"
        SIMILARITY_RESPONSE=$(curl -s -X POST "$SERVER_URL/api/boards/$BOARD_ID/evaluate")
        echo "응답: $SIMILARITY_RESPONSE"
        
        # 유사도 점수 추출
        SIMILARITY_SCORE=$(python3 -c "
        import json, sys
        try:
            data = json.loads('$SIMILARITY_RESPONSE')
            if data.get('success') and data.get('data') is not None:
                print(data['data'])
            else:
                print('평가 실패: ' + data.get('message', '알 수 없는 오류'))
        except Exception as e:
            print(f'파싱 오류: {e}')
        ")
        
        echo "이미지 유사도 점수: $SIMILARITY_SCORE"
    else
        echo "게시물 생성에 실패했습니다: $BOARD_ID"
    fi
fi

echo ""
echo "테스트 완료!" 