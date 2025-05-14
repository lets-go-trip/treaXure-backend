package com.trip.treaxure.vote.dto.response;

import java.time.LocalDate;

import com.trip.treaxure.vote.entity.Week;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "주차 응답 DTO")
public class WeekResponseDto {

    @Schema(description = "주차 ID", example = "1")
    private Integer weekId;

    @Schema(description = "시작일", example = "2025-05-05")
    private LocalDate weekStart;

    @Schema(description = "종료일", example = "2025-05-11")
    private LocalDate weekEnd;

    public static WeekResponseDto fromEntity(Week week) {
        return WeekResponseDto.builder()
                .weekId(week.getWeekId())
                .weekStart(week.getWeekStart())
                .weekEnd(week.getWeekEnd())
                .build();
    }
}
