package com.trip.treaxure.place.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "장소 생성 요청 DTO")
public class PlaceRequestDto {

    @Schema(description = "장소명", example = "서울타워")
    private String name;

    @Schema(description = "카테고리", example = "관광지")
    private String category;

    @Schema(description = "설명", example = "서울의 대표적인 관광지입니다.")
    private String description;

    @Schema(description = "주소", example = "서울 중구 남산공원길 105")
    private String address;

    @Schema(description = "위도", example = "37.5512")
    private Double latitude;

    @Schema(description = "경도", example = "126.9882")
    private Double longitude;

    @Schema(description = "대표 이미지 URL", example = "https://example.com/thumbnail.jpg")
    private String thumbnailUrl;
}
