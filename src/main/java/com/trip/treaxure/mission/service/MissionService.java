package com.trip.treaxure.mission.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;

/**
 * 미션 관련 비즈니스 로직 처리 서비스
 */
@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    /**
     * 전체 미션 목록 조회
     *
     * @return 모든 미션 목록
     */
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    /**
     * ID로 미션 조회
     *
     * @param id 미션 ID
     * @return 미션 Optional
     */
    public Optional<Mission> getMissionById(Long id) {
        return missionRepository.findById(id);
    }

    /**
     * 미션 등록
     *
     * @param mission 등록할 미션
     * @return 저장된 미션
     */
    public Mission createMission(Mission mission) {
        return missionRepository.save(mission);
    }

    /**
     * 미션 삭제
     *
     * @param id 삭제할 미션 ID
     */
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }
}
