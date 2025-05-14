package com.trip.treaxure.mission.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.mission.dto.MissionRequest;
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
    public List<Mission> getAllMissions() {
        return missionService.getAllMissions();
    }

    @Operation(summary = "미션 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미션 조회 성공"),
            @ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) {
        Optional<Mission> mission = missionService.getMissionById(id);
        return mission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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