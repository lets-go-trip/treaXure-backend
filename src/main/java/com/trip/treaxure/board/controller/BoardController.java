package com.trip.treaxure.board.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.auth.security.CustomUserDetails;
import com.trip.treaxure.board.dto.request.BoardRequestDto;
import com.trip.treaxure.board.dto.response.BoardResponseDto;
import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.service.BoardService;
import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.member.entity.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/boards")
@Tag(name = "Board Controller", description = "게시물 관리를 위한 API")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    @Operation(summary = "전체 게시물 조회")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<BoardResponseDto>>> getAllBoards() {
        return ResponseEntity.ok(ApiResponseDto.success(boardService.getAllBoards()));
    }

    @Operation(summary = "게시물 ID로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> getBoardById(@PathVariable Integer id) {
        return boardService.getBoardById(id)
                .map(ApiResponseDto::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    @Operation(summary = "내 전체 게시글 조회")
    public ResponseEntity<ApiResponseDto<List<BoardResponseDto>>> getMyBoards(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Member member = userDetails.getMember();  // 또는 getMemberId()
        List<Board> boards = boardService.getBoardsByMember(member.getMemberId());
        List<BoardResponseDto> dtos = boards.stream().map(BoardResponseDto::fromEntity).toList();
        return ResponseEntity.ok(ApiResponseDto.success(dtos));
    }

    @GetMapping("/my/{missionId}")
    @Operation(summary = "미션별 내 게시글 단건 조회")
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> getMyBoardByMissionId(
            @PathVariable("missionId") Long missionId,
            @AuthenticationPrincipal Member member
    ) {
        Optional<Board> board = boardService.getBoardByMissionAndMember(missionId, member.getMemberId());
        return board
                .map(b -> ResponseEntity.ok(ApiResponseDto.success(BoardResponseDto.fromEntity(b))))
                .orElseGet(() -> ResponseEntity.ok(ApiResponseDto.success(null))); // 없으면 null 응답
    }

    @Operation(summary = "게시물 생성")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시물 생성 성공"),
        @ApiResponse(responseCode = "400", description = "요청 유효성 실패")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<BoardResponseDto>> createBoard(
        @Valid @RequestBody BoardRequestDto dto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        dto.setMemberId(userDetails.getMember().getMemberId());
        BoardResponseDto createdBoard = boardService.createBoard(dto);
        dto.setMemberId(userDetails.getMember().getMemberId());
        
        // 게시물 생성 후 자동으로 유사도 평가 수행
        try {
            Float similarityScore = boardService.evaluateImageSimilarity(
                    createdBoard.getMissionId(), 
                    Integer.valueOf(createdBoard.getBoardId())
            );

            log.info("Board created with ID: {} and similarity score: {}", 
                    createdBoard.getBoardId(), similarityScore);
        } catch (Exception e) {
            // 유사도 평가 실패 시에도 게시물 생성은 성공으로 처리
            log.error("Failed to evaluate image similarity for board ID: {}", 
                    createdBoard.getBoardId(), e);
        }
        
        return ResponseEntity.ok(ApiResponseDto.success(createdBoard));
    }

    @Operation(summary = "이미지 유사도 평가", description = "게시물 이미지와 미션의 레퍼런스 이미지 간의 유사도를 평가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유사도 평가 성공"),
        @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없음")
    })
    @PostMapping("/{boardId}/evaluate")
    public ResponseEntity<ApiResponseDto<Float>> evaluateImageSimilarity(@PathVariable Integer boardId) {
        BoardResponseDto board = boardService.getBoardById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        
        Float similarityScore = boardService.evaluateImageSimilarity(
                board.getMissionId(), boardId);
        
        return ResponseEntity.ok(ApiResponseDto.success(similarityScore));
    }

    @Operation(summary = "게시물 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteBoard(@PathVariable Integer id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

}
