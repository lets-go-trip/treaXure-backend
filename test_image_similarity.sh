#!/bin/bash

# Two image URLs to compare
REFERENCE_URL="https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg"
TARGET_URL="https://i.namu.wiki/i/DEvKxYg-TEz6O53jeZyS9kndJSgSQnFysm3T-R70yXIyWi9-HknJZXoK1ghHFMwB365TyyMj7MlIebAKMrLSFA.webp"

# Create a temporary directory for curl output
mkdir -p tmp

# Create a mission with a reference image
echo "Creating a mission with reference image..."
MISSION_RESPONSE=$(curl -s -X POST http://localhost:8081/api/missions \
  -H "Content-Type: application/json" \
  -d '{
    "placeId": 1,
    "memberId": 1,
    "title": "Test Mission",
    "description": "Test mission for image similarity",
    "type": "PHOTO",
    "score": 10,
    "referenceUrl": "'"$REFERENCE_URL"'",
    "status": "PENDING"
  }')

echo "Mission response: $MISSION_RESPONSE"
MISSION_ID=$(echo $MISSION_RESPONSE | grep -o '"missionId":[0-9]*' | cut -d ":" -f2)
echo "Mission ID: $MISSION_ID"

# Create a board with a target image
echo "Creating a board with target image..."
BOARD_RESPONSE=$(curl -s -X POST http://localhost:8081/api/boards \
  -H "Content-Type: application/json" \
  -d '{
    "missionId": '"$MISSION_ID"',
    "imageUrl": "'"$TARGET_URL"'",
    "title": "Test board for image similarity"
  }')

echo "Board response: $BOARD_RESPONSE"
BOARD_ID=$(echo $BOARD_RESPONSE | grep -o '"boardId":[0-9]*' | cut -d ":" -f2)
echo "Board ID: $BOARD_ID"

# Test image similarity evaluation directly
echo "Testing image similarity evaluation..."
SIMILARITY_RESPONSE=$(curl -s -X POST http://localhost:8081/api/boards/$BOARD_ID/evaluate)

echo "Similarity response: $SIMILARITY_RESPONSE"
SIMILARITY_SCORE=$(echo $SIMILARITY_RESPONSE | grep -o '"data":[0-9.]*' | cut -d ":" -f2)
echo "Similarity score: $SIMILARITY_SCORE"

echo "Test completed!" 