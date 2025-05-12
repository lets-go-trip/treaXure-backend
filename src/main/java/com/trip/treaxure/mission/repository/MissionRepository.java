package com.trip.treaxure.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.mission.entity.Mission;

/**
 * 미션 관련 DB 작업을 처리하는 JPA 리포지토리
 */
public interface MissionRepository extends JpaRepository<Mission, Long> {

    /**
     * 특정 장소의 미션 조회
     *
     * @param placeId 장소 ID
     * @return 해당 장소의 미션 목록
     */
    List<Mission> findByPlaceId(Long placeId);

    /**
     * 특정 사용자의 미션 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자가 생성한 미션 목록
     */
    List<Mission> findByUserId(Long userId);

    /**
     * 상태별 미션 조회
     *
     * @param status 미션 상태
     * @return 해당 상태의 미션 목록
     */
    List<Mission> findByStatus(Mission.MissionStatus status);

    /**
     * 유형별 미션 조회
     *
     * @param type 미션 유형
     * @return 해당 유형의 미션 목록
     */
    List<Mission> findByType(Mission.MissionType type);
}
