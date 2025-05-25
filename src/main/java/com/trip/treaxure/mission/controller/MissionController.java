package com.trip.treaxure.mission.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.mission.dto.request.MissionRequestDto;
import com.trip.treaxure.mission.dto.response.MissionResponseDto;
import com.trip.treaxure.mission.dto.response.PlaceMissionStatusDto;
import com.trip.treaxure.mission.service.MissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Mission Controller", description = "미션 관리를 위한 API")
public class MissionController {

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @Operation(summary = "전체 미션 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 리스트 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<MissionResponseDto>>> getAllMissions() {
        return ResponseEntity.ok(ApiResponseDto.success(missionService.getAllMissions()));
    }

    @Operation(summary = "미션 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 조회 성공"),
            @ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<MissionResponseDto>> getMissionById(@PathVariable Long id) {
        return missionService.getMissionById(id)
                .map(ApiResponseDto::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "장소 ID로 미션 목록 조회")
    @GetMapping("/place/{placeId}")
    public ResponseEntity<ApiResponseDto<List<MissionResponseDto>>> getMissionsByPlace(@PathVariable("placeId") Long placeId) {
        List<MissionResponseDto> missions = missionService.getMissionsByPlaceId(placeId);
        return ResponseEntity.ok(ApiResponseDto.success(missions));
    }

    @Operation(summary = "미션 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 생성 성공")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<MissionResponseDto>> createMission(@RequestBody MissionRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponseDto.success(missionService.createMission(requestDto)));
    }

    @Operation(summary = "미션 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "미션 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    @Operation(summary = "사용자의 장소별 미션 완료 현황 조회")
    @GetMapping("/place-status/{memberId}")
    public ResponseEntity<ApiResponseDto<List<PlaceMissionStatusDto>>> getMissionStatusByPlace(
            @PathVariable Long memberId
    ) {
        List<PlaceMissionStatusDto> result = missionService.getMissionCompletionStatusByMember(memberId);
        return ResponseEntity.ok(ApiResponseDto.success(result));
    }

}
