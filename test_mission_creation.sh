#!/bin/bash

echo "Creating test place with ID 3..."
PLACE_RESPONSE=$(curl -s -X 'POST' \
  'http://localhost:8081/api/places' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "북촌 한옥마을", 
    "address": "서울특별시 종로구 계동길 37", 
    "latitude": 37.5796, 
    "longitude": 126.9856, 
    "category": "HISTORICAL", 
    "description": "전통 한옥이 보존되어 있는 서울의 역사적인 지역",
    "thumbnailUrl": "https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg"
  }')

echo "Place response: $PLACE_RESPONSE"

echo -e "\nCreating test mission..."
MISSION_RESPONSE=$(curl -s -X 'POST' \
  'http://localhost:8081/api/missions' \
  -H 'Content-Type: application/json' \
  -d '{
    "placeId": 1,
    "memberId": 1,
    "title": "북촌 한옥마을에 가면....",
    "description": "북촌 한옥마을에 가면 이 사진은 남겨야합니다.",
    "type": "PHOTO",
    "score": 10,
    "referenceUrl": "https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg",
    "status": "PENDING"
  }')

echo "Mission response: $MISSION_RESPONSE" 