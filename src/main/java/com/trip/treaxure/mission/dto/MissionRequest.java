package com.trip.treaxure.mission.dto;

import com.trip.treaxure.mission.entity.Mission.MissionStatus;
import com.trip.treaxure.mission.entity.Mission.MissionType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "미션 생성 요청 DTO")
public record MissionRequest(
    @Schema(description = "장소 ID", example = "1", required = true)
    Long placeId,

    @Schema(description = "사용자 ID", example = "1", required = true)
    Long memberId,

    @Schema(description = "미션 제목", example = "서울타워 방문", required = true)
    String title,

    @Schema(description = "미션 설명", example = "서울타워에 방문해 사진을 찍으세요.", required = true)
    String description,

    @Schema(description = "미션 타입", example = "PHOTO", required = true)
    MissionType type,

    @Schema(description = "미션 점수", example = "10", required = true)
    Integer score,

    @Schema(description = "참고 이미지 URL", example = "https://example.com/mission.jpg", required = true)
    String referenceUrl,

    @Schema(description = "미션 상태", example = "PENDING", required = true)
    MissionStatus status,

    @Schema(description = "활성 여부", example = "true")
    Boolean isActive
) {}