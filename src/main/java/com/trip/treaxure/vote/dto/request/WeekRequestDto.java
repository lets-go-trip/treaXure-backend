package com.trip.treaxure.vote.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "주차 생성 요청 DTO")
public class WeekRequestDto {

    @Schema(description = "시작일", example = "2025-05-05")
    private LocalDate weekStart;

    @Schema(description = "종료일", example = "2025-05-11")
    private LocalDate weekEnd;
}
