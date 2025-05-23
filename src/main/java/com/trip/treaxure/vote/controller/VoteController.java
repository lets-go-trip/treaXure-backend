package com.trip.treaxure.vote.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.vote.dto.request.VoteRequestDto;
import com.trip.treaxure.vote.dto.response.VoteResponseDto;
import com.trip.treaxure.vote.service.VoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "Vote Controller", description = "투표(Vote) 기능 제공")
public class VoteController {

    private final VoteService voteService;

    @Operation(summary = "전체 투표 조회")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<VoteResponseDto>>> getAllVotes() {
        return ResponseEntity.ok(ApiResponseDto.success(voteService.getAllVotes()));
    }

    @Operation(summary = "투표 ID로 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<VoteResponseDto>> getVoteById(@PathVariable Long id) {
        return voteService.getVoteById(id)
                .map(ApiResponseDto::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "투표 생성")
    @PostMapping
    public ResponseEntity<ApiResponseDto<VoteResponseDto>> createVote(@RequestBody VoteRequestDto dto) {
        return ResponseEntity.ok(ApiResponseDto.success(voteService.createVote(dto)));
    }

    @Operation(summary = "투표 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteVote(@PathVariable Long id) {
        voteService.deleteVote(id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }
}
