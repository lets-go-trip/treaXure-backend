package com.trip.treaxure.favorite.dto.response;

import java.time.LocalDateTime;

import com.trip.treaxure.board.dto.response.BoardResponseDto;
import com.trip.treaxure.favorite.entity.Favorite;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "좋아요 응답 DTO")
public class FavoriteResponseDto {

    @Schema(description = "좋아요 ID", example = "1")
    private Long favoriteId;

    @Schema(description = "게시물 ID", example = "1")
    private Long boardId;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "생성 시각", example = "2025-05-11T14:00:00")
    private LocalDateTime createdAt;

    private BoardResponseDto board;

    public static FavoriteResponseDto fromEntity(Favorite favorite) {
        return FavoriteResponseDto.builder()
                .favoriteId(favorite.getFavoriteId())
                .boardId(favorite.getBoard().getBoardId().longValue())
                .memberId(favorite.getMember().getMemberId().longValue())
                .createdAt(favorite.getCreatedAt())
                .board(BoardResponseDto.fromEntity(favorite.getBoard()))
                .build();
    }
}
