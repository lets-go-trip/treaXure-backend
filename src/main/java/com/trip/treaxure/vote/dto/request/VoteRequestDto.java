package com.trip.treaxure.vote.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "투표 생성 요청 DTO")
public class VoteRequestDto {

    @Schema(description = "주차 ID", example = "1")
    private Long weekId;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "게시물 ID", example = "1")
    private Long boardId;

    @Schema(description = "투표 필드", example = "best")
    private String field;
}
