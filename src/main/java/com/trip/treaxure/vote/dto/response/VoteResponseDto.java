package com.trip.treaxure.vote.dto.response;

import com.trip.treaxure.vote.entity.Vote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "투표 응답 DTO")
public class VoteResponseDto {

    @Schema(description = "투표 ID", example = "1")
    private Long voteId;

    @Schema(description = "주차 ID", example = "1")
    private Integer weekId;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "게시물 ID", example = "1")
    private Integer boardId;

    @Schema(description = "투표 필드", example = "best")
    private String field;

    public static VoteResponseDto fromEntity(Vote vote) {
        return VoteResponseDto.builder()
                .voteId(vote.getVoteId())
                .weekId(vote.getWeekId().getWeekId())
                .memberId(vote.getMemberId().getMemberId())
                .boardId(vote.getBoardId().getBoardId())
                .field(vote.getField())
                .build();
    }
}
