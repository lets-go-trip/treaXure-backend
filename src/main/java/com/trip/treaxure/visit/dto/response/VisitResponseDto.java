package com.trip.treaxure.visit.dto.response;

import java.time.LocalDateTime;

import com.trip.treaxure.visit.entity.Visit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "방문 응답 DTO")
public class VisitResponseDto {

    @Schema(description = "방문 기록 ID", example = "1")
    private Integer visitId;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "장소 ID", example = "10")
    private Integer placeId;

    @Schema(description = "방문 횟수", example = "3")
    private Integer visitCount;

    @Schema(description = "첫 방문 시각")
    private LocalDateTime createdAt;

    @Schema(description = "최근 방문 시각")
    private LocalDateTime updatedAt;

    public static VisitResponseDto fromEntity(Visit visit) {
        return VisitResponseDto.builder()
                .visitId(visit.getVisitId())
                .memberId(visit.getMember().getMemberId())
                .placeId(visit.getPlace().getPlaceId())
                .visitCount(visit.getVisitCount())
                .createdAt(visit.getCreatedAt())
                .updatedAt(visit.getUpdatedAt())
                .build();
    }
}
