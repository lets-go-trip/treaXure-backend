package com.trip.xplore.mission.controller;

import com.trip.xplore.mission.entity.Mission;
import com.trip.xplore.mission.service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    @Autowired
    private MissionService missionService;

    @GetMapping
    public List<Mission> getAllMissions() {
        return missionService.getAllMissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mission> getMissionById(@PathVariable Long id) {
        Optional<Mission> mission = missionService.getMissionById(id);
        return mission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mission createMission(@RequestBody Mission mission) {
        return missionService.createMission(mission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
} 