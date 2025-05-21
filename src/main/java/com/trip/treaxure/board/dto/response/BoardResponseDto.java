package com.trip.treaxure.board.dto.response;

import java.time.LocalDateTime;

import com.trip.treaxure.board.entity.Board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게시물 응답 DTO")
public class BoardResponseDto {

    @Schema(description = "게시물 ID", example = "1")
    private Integer boardId;

    @Schema(description = "작성자 ID", example = "1")
    private Long memberId;

    @Schema(description = "미션 ID", example = "1")
    private Long missionId;

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "좋아요 수", example = "5")
    private Integer favoriteCount;

    @Schema(description = "작성 시각", example = "2025-05-11T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "활성 여부", example = "true")
    private Boolean isActive;

    @Schema(description = "게시물 제목", example = "오늘의 여행 사진")
    private String title;

    public static BoardResponseDto fromEntity(Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .memberId(board.getMemberId())
                .missionId(board.getMission().getMissionId())
                .imageUrl(board.getImageUrl())
                .favoriteCount(board.getFavoriteCount())
                .createdAt(board.getCreatedAt())
                .isActive(board.getIsActive())
                .title(board.getTitle())
                .build();
    }
}
