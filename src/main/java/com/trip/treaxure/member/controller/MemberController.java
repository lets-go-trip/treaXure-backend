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
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
