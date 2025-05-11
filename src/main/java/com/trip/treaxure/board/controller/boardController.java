package com.trip.treaxure.board.controller;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.service.boardService;

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
@RequestMapping("/api/boards")
@Tag(name = "Board Controller", description = "게시물 관리를 위한 API")
public class boardController {

    @Autowired
    private boardService boardService;

    @Operation(summary = "전체 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 리스트 조회 성공")
    })
    @GetMapping
    public List<Board> getAllboards() {
        return boardService.getAllboards();
    }

    @Operation(summary = "게시물 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Board> getboardById(@PathVariable Long id) {
        Optional<Board> board = boardService.getboardById(id);
        return board.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "게시물 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 생성 성공")
    })
    @PostMapping
    public Board createboard(@RequestBody Board board) {
        return boardService.createboard(board);
    }

    @Operation(summary = "게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시물 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteboard(@PathVariable Long id) {
        boardService.deleteboard(id);
        return ResponseEntity.noContent().build();
    }
} 