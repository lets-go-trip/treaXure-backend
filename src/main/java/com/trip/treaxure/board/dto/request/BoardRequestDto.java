package com.trip.treaxure.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시물 생성 요청 DTO")
public class BoardRequestDto {

    @Schema(description = "미션 ID", example = "1")
    private Long missionId;

    @Schema(description = "사진 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "게시물 제목", example = "오늘의 여행 사진")
    private String title;
}
