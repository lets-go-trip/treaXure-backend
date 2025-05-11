package com.trip.treaxure.vote.controller;

import com.trip.treaxure.vote.entity.Vote;
import com.trip.treaxure.vote.service.VoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/votes")
@Tag(name = "VoteController", description = "투표(Vote) 기능 제공")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Operation(summary = "전체 투표 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투표 리스트 조회 성공")
    })
    @GetMapping
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }

    @Operation(summary = "투표 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투표 조회 성공"),
            @ApiResponse(responseCode = "404", description = "투표를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable Long id) {
        Optional<Vote> vote = voteService.getVoteById(id);
        return vote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "투표 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투표 생성 성공")
    })
    @PostMapping
    public Vote createVote(@RequestBody Vote vote) {
        return voteService.createVote(vote);
    }

    @Operation(summary = "투표 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "투표 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
        voteService.deleteVote(id);
        return ResponseEntity.noContent().build();
    }
} 