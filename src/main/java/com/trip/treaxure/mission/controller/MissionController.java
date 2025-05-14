package com.trip.treaxure.mission.controller;

import com.trip.treaxure.mission.dto.request.MissionRequestDto;
import com.trip.treaxure.mission.dto.response.MissionResponseDto;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.service.MissionService;

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
@RequestMapping("/api/missions")
@Tag(name = "Mission Controller", description = "미션 관리를 위한 API")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @Operation(summary = "전체 미션 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 리스트 조회 성공")
    })
    @GetMapping
    public List<MissionResponseDto> getAllMissions() {
        return missionService.getAllMissions();
    }

    @Operation(summary = "미션 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 조회 성공"),
            @ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MissionResponseDto> getMissionById(@PathVariable Long id) {
        Optional<MissionResponseDto> mission = missionService.getMissionById(id);
        return mission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "미션 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 생성 성공")
    })
    public MissionResponseDto createMission(@RequestBody MissionRequestDto requestDto) {
        return missionService.createMission(requestDto);
    }

    @Operation(summary = "미션 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "미션 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
}
