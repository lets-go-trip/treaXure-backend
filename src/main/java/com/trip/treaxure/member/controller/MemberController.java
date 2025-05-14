package com.trip.treaxure.member.controller;

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
import com.trip.treaxure.member.dto.request.MemberRequestDto;
import com.trip.treaxure.member.dto.response.MemberResponseDto;
import com.trip.treaxure.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member Controller", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    // Todo: Member 관련 확인 필요
  
    @Operation(summary = "모든 회원 조회")
    @GetMapping
    public List<MemberResponseDto> getAllMembers() {
        return memberService.getAllMembers();
    }

    @Operation(summary = "회원 ID로 조회")
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "회원 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 생성 성공")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<MemberResponseDto>> createMember(@RequestBody MemberRequestDto dto) {
        MemberResponseDto response = memberService.createMember(dto);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "회원 삭제")
    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제", description = "특정 회원을 삭제합니다 (관리자 전용)")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
  
    // Todo: Board 관련 확인 필요

    @GetMapping("/me/boards")
    @Operation(
        summary = "내 게시글 목록 조회",
        description = "현재 로그인한 회원이 작성한 게시글 목록을 조회합니다"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BoardListResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        )
    })
    public ResponseEntity<BoardListResponse> getMyBoards(
            @AuthenticationPrincipal UserDetails userDetails) {
        BoardListResponse response = memberService.getMyBoards(userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/boards/{boardId}")
    @Operation(
        summary = "내 게시글 상세 조회",
        description = "현재 로그인한 회원이, 작성한 특정 게시글의 상세 정보를 조회합니다"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BoardDetailResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "게시글을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        )
    })
    public ResponseEntity<BoardDetailResponse> getMyBoardDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "조회할 게시글 ID", required = true)
            @PathVariable Integer boardId) {
        BoardDetailResponse response = memberService.getMyBoardDetail(userDetails, boardId);
        return ResponseEntity.ok(response);
    }
} 
