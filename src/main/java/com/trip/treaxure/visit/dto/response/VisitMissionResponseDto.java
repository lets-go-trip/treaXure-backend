package com.trip.treaxure.visit.dto.response;

import java.util.List;

import com.trip.treaxure.mission.dto.response.MissionResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자의 방문 장소 및 해당 장소의 미션 목록 DTO")
public class VisitMissionResponseDto {

    @Schema(description = "방문한 장소 ID", example = "1")
    private Long placeId;

    @Schema(description = "방문한 장소 이름", example = "경복궁")
    private String placeName;

    @Schema(description = "방문한 장소 주소", example = "서울 종로구 사직로 161")
    private String address;

    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg")
    private String thumbnailUrl;

    @Schema(description = "해당 장소의 미션 목록")
    private List<MissionResponseDto> missions;
}
