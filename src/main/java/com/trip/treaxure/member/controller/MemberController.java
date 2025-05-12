package com.trip.treaxure.member.controller;

import com.trip.treaxure.member.dto.BoardDetailResponse;
import com.trip.treaxure.member.dto.BoardListResponse;
import com.trip.treaxure.member.dto.MemberProfileResponse;
import com.trip.treaxure.member.dto.UpdateProfileRequest;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.service.MemberService;

import io.swagger.v3.oas.annotations.*;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 관련 API")
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    @Operation(summary = "전체 회원 조회", description = "시스템에 등록된 모든 회원을 조회합니다 (관리자 전용)")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID로 회원 조회", description = "회원 ID로 특정 회원을 조회합니다 (관리자 전용)")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.getMemberById(id);
        return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다 (관리자 전용)")
    public Member createMember(@RequestBody Member member) {
        return memberService.createMember(member);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제", description = "특정 회원을 삭제합니다 (관리자 전용)")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(
        summary = "내 프로필 조회",
        description = "현재 로그인한 회원의 프로필 정보를 조회합니다"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = MemberProfileResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        )
    })
    public ResponseEntity<MemberProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        MemberProfileResponse response = memberService.getMyProfile(userDetails);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @Operation(
        summary = "내 프로필 수정",
        description = "현재 로그인한 회원의 프로필 정보를 수정합니다"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "유효성 검사 실패",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        )
    })
    public ResponseEntity<com.trip.treaxure.common.ApiResponse<?>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        memberService.updateMyProfile(userDetails, request);
        return ResponseEntity.ok(com.trip.treaxure.common.ApiResponse.success("Profile updated successfully."));
    }

    @GetMapping("/{nickname}")
    @Operation(
        summary = "닉네임으로 회원 프로필 조회",
        description = "특정 닉네임을 가진 회원의 프로필 정보를 조회합니다"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = MemberProfileResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "회원을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = com.trip.treaxure.common.ApiResponse.class))
        )
    })
    public ResponseEntity<MemberProfileResponse> getMemberProfile(
            @Parameter(description = "조회할 회원의 닉네임", required = true)
            @PathVariable String nickname) {
        MemberProfileResponse response = memberService.getMemberProfileByNickname(nickname);
        return ResponseEntity.ok(response);
    }

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