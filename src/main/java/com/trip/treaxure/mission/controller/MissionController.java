package com.trip.treaxure.mission.controller;

import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.mission.dto.request.MissionRequestDto;
import com.trip.treaxure.mission.dto.response.MissionResponseDto;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.service.MissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Mission Controller", description = "미션 관리를 위한 API")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @Operation(summary = "미션 생성")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "미션 생성 성공"),
        @ApiResponse(responseCode = "404", description = "장소 또는 사용자를 찾을 수 없음")
    })
    @PostMapping
    public Mission createMission(@RequestBody MissionRequest request) {
        return missionService.createMissionFromDto(request);
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
}
