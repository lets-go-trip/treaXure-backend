package com.trip.treaxure.mission.repository;

import com.trip.treaxure.mission.entity.Mission;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    /**
     * 특정 장소의 미션 조회
     *   Place 엔티티의 placeId 필드를 비교하도록 JPQL 작성
     */
    @Query("SELECT m FROM Mission m WHERE m.placeId.placeId = :placeId")
    List<Mission> findByPlaceId(@Param("placeId") Long placeId);

    /**
     * 특정 사용자의 미션 조회
     */
    @Query("SELECT m FROM Mission m WHERE m.memberId.memberId = :userId")
    List<Mission> findByUserId(@Param("userId") Long userId);

    /**
     * 상태별 미션 조회
     */
    List<Mission> findByStatus(Mission.MissionStatus status);

    /**
     * 유형별 미션 조회
     */
    List<Mission> findByType(Mission.MissionType type);
}
