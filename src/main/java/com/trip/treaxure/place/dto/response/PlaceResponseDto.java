package com.trip.treaxure.place.dto.response;

import java.time.LocalDateTime;

import com.trip.treaxure.place.entity.Place;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "장소 응답 DTO")
public class PlaceResponseDto {

    @Schema(description = "장소 ID", example = "1")
    private Integer placeId;

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

    @Schema(description = "등록 시각", example = "2025-05-11T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시각", example = "2025-05-12T09:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "활성 여부", example = "true")
    private Boolean isActive;

    public static PlaceResponseDto fromEntity(Place place) {
        return PlaceResponseDto.builder()
                .placeId(place.getPlaceId())
                .name(place.getName())
                .category(place.getCategory())
                .description(place.getDescription())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .thumbnailUrl(place.getThumbnailUrl())
                .createdAt(place.getCreatedAt())
                .updatedAt(place.getUpdatedAt())
                .isActive(place.getIsActive())
                .build();
    }
}
