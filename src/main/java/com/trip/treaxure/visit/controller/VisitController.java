package com.trip.treaxure.visit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.visit.dto.response.VisitMissionResponseDto;
import com.trip.treaxure.visit.dto.response.VisitResponseDto;
import com.trip.treaxure.visit.service.VisitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
@Tag(name = "Visit Controller", description = "장소 방문 기록 API")
public class VisitController {

    private final VisitService visitService;

    @Operation(summary = "장소 방문 기록")
    @PostMapping("/{memberId}/{placeId}")
    public ResponseEntity<ApiResponseDto<VisitResponseDto>> recordVisit(
            @PathVariable Long memberId,
            @PathVariable Long placeId) {

        VisitResponseDto response = visitService.recordVisit(memberId, placeId);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "특정 장소의 방문 기록 조회")
    @GetMapping("/place/{placeId}")
    public ResponseEntity<ApiResponseDto<List<VisitResponseDto>>> getVisitsByPlace(
            @PathVariable Long placeId) {
        return ResponseEntity.ok(ApiResponseDto.success(visitService.getVisitsByPlaceId(placeId)));
    }

    @Operation(summary = "특정 사용자의 방문 기록 조회")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponseDto<List<VisitResponseDto>>> getVisitsByMember(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(ApiResponseDto.success(visitService.getVisitsByMemberId(memberId)));
    }

    @Operation(summary = "사용자의 방문 장소 및 해당 장소의 미션 목록 조회")
    @GetMapping("/member/{memberId}/missions")
    public ResponseEntity<ApiResponseDto<List<VisitMissionResponseDto>>> getVisitMissionsByMember(
            @PathVariable Long memberId) {
        List<VisitMissionResponseDto> result = visitService.getVisitMissionsByMember(memberId);
        return ResponseEntity.ok(ApiResponseDto.success(result));
    }

}
