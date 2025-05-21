package com.trip.treaxure.mission.dto.response;

import com.trip.treaxure.mission.entity.Mission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "미션 응답 DTO")
public class MissionResponseDto {

    @Schema(description = "미션 ID", example = "1")
    private Long missionId;

    @Schema(description = "장소 ID", example = "101")
    private Integer placeId;

    @Schema(description = "회원 ID", example = "501")
    private Long memberId;

    @Schema(description = "미션 제목", example = "서울타워 방문")
    private String title;

    @Schema(description = "미션 설명", example = "서울타워에 방문해 사진을 찍으세요.")
    private String description;

    @Schema(description = "미션 타입", example = "PHOTO")
    private Mission.MissionType type;

    @Schema(description = "미션 점수", example = "10")
    private Integer score;

    @Schema(description = "참고 이미지 URL", example = "https://example.com/mission.jpg")
    private String referenceUrl;

    @Schema(description = "미션 상태", example = "PENDING")
    private Mission.MissionStatus status;

    @Schema(description = "생성 시각", example = "2025-05-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "평가 시각", example = "2025-05-02T00:00:00")
    private LocalDateTime evaluatedAt;

    @Schema(description = "활성 상태", example = "true")
    private Boolean isActive;

    public static MissionResponseDto fromEntity(Mission mission) {
        return MissionResponseDto.builder()
                .missionId(mission.getMissionId())
                .placeId(mission.getPlace().getPlaceId())
                .memberId(mission.getMember().getMemberId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .type(mission.getType())
                .score(mission.getScore())
                .referenceUrl(mission.getReferenceUrl())
                .status(mission.getStatus())
                .createdAt(mission.getCreatedAt())
                .evaluatedAt(mission.getEvaluatedAt())
                .isActive(mission.getIsActive())
                .build();
    }
}
