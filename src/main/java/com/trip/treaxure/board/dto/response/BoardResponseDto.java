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

    @Schema(description = "작성자 닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "작성자 프로필 이미지", example = "https://example.com/profile.jpg")
    private String profileUrl;

    @Schema(description = "미션 ID", example = "1")
    private Long missionId;

    @Schema(description = "장소 이름", example = "경복궁")
    private String placeName;

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

    @Schema(description = "이미지 유사도 점수", example = "0.8234")
    private Float similarityScore;

    public static BoardResponseDto fromEntity(Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .memberId(board.getMemberId())
                .nickname(board.getMember() != null ? board.getMember().getNickname() : null)
                .profileUrl(board.getMember() != null ? board.getMember().getProfileUrl() : null)
                .missionId(board.getMission().getMissionId())
                .placeName(board.getMission().getPlace().getName())
                .imageUrl(board.getImageUrl())
                .favoriteCount(board.getFavoriteCount())
                .createdAt(board.getCreatedAt())
                .isActive(board.getIsActive())
                .title(board.getTitle())
                .similarityScore(board.getSimilarityScore())
                .build();
    }
}
