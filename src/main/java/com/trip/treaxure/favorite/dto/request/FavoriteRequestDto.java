package com.trip.treaxure.favorite.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "좋아요 생성 요청 DTO")
public class FavoriteRequestDto {

    @Schema(description = "게시물 ID", example = "1")
    private Long boardId;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;
}
