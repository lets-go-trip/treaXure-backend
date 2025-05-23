package com.trip.treaxure.mission.dto.request;

import java.time.LocalDateTime;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.entity.Mission.MissionStatus;
import com.trip.treaxure.mission.entity.Mission.MissionType;
import com.trip.treaxure.place.entity.Place;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "미션 생성 요청 DTO")
@Getter
@Setter
public class MissionRequestDto {

    @Schema(description = "장소 ID", example = "1", required = true)
    private Long placeId;

    @Schema(description = "회원 ID", example = "1", required = true)
    private Long memberId;

    @Schema(description = "미션 제목", example = "서울타워 방문", required = true)
    private String title;

    @Schema(description = "미션 설명", example = "서울타워에 방문해 사진을 찍으세요.", required = true)
    private String description;

    @Schema(description = "미션 타입", example = "PHOTO", required = true)
    private MissionType type;

    @Schema(description = "미션 점수", example = "100", required = true)
    private Integer score;

    @Schema(description = "참고 이미지 URL", example = "https://example.com/image.jpg", required = true)
    private String referenceUrl;

    @Schema(description = "미션 상태", example = "PENDING", required = true)
    private MissionStatus status;

    public Mission toEntity(Member member, Place place) {
    return Mission.builder()
            .title(this.title)
            .description(this.description)
            .type(this.type)
            .score(this.score)
            .referenceUrl(this.referenceUrl)
            .status(this.status)
            .createdAt(LocalDateTime.now()) // 이 부분 추가
            .evaluatedAt(LocalDateTime.now()) // 필요 시 함께
            .place(place)
            .member(member)
            .isActive(true)
            .build();
}
}
