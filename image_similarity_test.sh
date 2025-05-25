#!/bin/bash

# 이미지 유사도 테스트 스크립트
echo "=== TreaXure 이미지 유사도 테스트 도구 ==="

# 인자 확인
if [ "$#" -ne 2 ]; then
    echo "사용법: $0 <첫번째_이미지_경로> <두번째_이미지_경로>"
    echo "예시: $0 https://example.com/image1.jpg https://example.com/image2.jpg"
    echo "또는: $0 ./image1.jpg ./image2.jpg"
    
    # 인자가 없으면 대화형 모드 실행
    echo ""
    echo "대화형 모드로 실행합니다."
    java -cp target/classes:target/test-classes com.trip.treaxure.global.util.ImageSimilarityTestTool
    exit 0
fi

# 인자로 전달된 이미지 경로
IMAGE1="$1"
IMAGE2="$2"

# 프로젝트가 빌드되었는지 확인
if [ ! -d "target/classes" ]; then
    echo "프로젝트를 먼저 빌드해야 합니다."
    echo "다음 명령을 실행하세요: ./gradlew build"
    exit 1
fi

# 환경 변수 설정 및 애플리케이션 실행
echo "첫 번째 이미지: $IMAGE1"
echo "두 번째 이미지: $IMAGE2"
echo ""

# Java 프로그램 실행 (입력을 파이프로 전달)
(echo "$IMAGE1"; echo "$IMAGE2") | java -cp target/classes:target/test-classes com.trip.treaxure.global.util.ImageSimilarityTestTool

echo ""
echo "테스트 완료!" 