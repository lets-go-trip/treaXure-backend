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
import com.trip.treaxure.vote.dto.request.WeekRequestDto;
import com.trip.treaxure.vote.dto.response.WeekResponseDto;
import com.trip.treaxure.vote.service.WeekService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/weeks")
@RequiredArgsConstructor
@Tag(name = "Week Controller", description = "주차(Week) 정보 관리 API")
public class WeekController {

    private final WeekService weekService;

    @Operation(summary = "전체 주차 조회")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<WeekResponseDto>>> getAllWeeks() {
        return ResponseEntity.ok(ApiResponseDto.success(weekService.getAllWeeks()));
    }

    @Operation(summary = "주차 ID로 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<WeekResponseDto>> getWeekById(@PathVariable Long id) {
        return weekService.getWeekById(id)
                .map(ApiResponseDto::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @Operation(summary = "주차 생성")
    @PostMapping
    public ResponseEntity<ApiResponseDto<WeekResponseDto>> createWeek(@RequestBody WeekRequestDto dto) {
        return ResponseEntity.ok(ApiResponseDto.success(weekService.createWeek(dto)));
    }

    @Operation(summary = "주차 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteWeek(@PathVariable Long id) {
        weekService.deleteWeek(id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }
}
