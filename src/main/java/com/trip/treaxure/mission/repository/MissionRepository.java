package com.trip.treaxure.mission.repository;

import com.trip.treaxure.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    // TODO: Add custom queries if needed
} 