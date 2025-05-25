package com.trip.treaxure.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trip.treaxure.mission.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    // 장소 ID 기준 전체 미션 조회 (개별 장소 조회 시 사용)
    @Query("SELECT m FROM Mission m JOIN FETCH m.place WHERE m.place.placeId = :placeId")
    List<Mission> findAllByPlace_PlaceId(@Param("placeId") Long placeId);

    // 사용자가 만든 미션 조회
    @Query("SELECT m FROM Mission m JOIN FETCH m.member WHERE m.member.memberId = :memberId")
    List<Mission> findByMemberId(@Param("memberId") Long memberId);

    // 상태 기준
    List<Mission> findByStatus(Mission.MissionStatus status);

    // 타입 기준
    List<Mission> findByType(Mission.MissionType type);

    // 전체 미션 + 장소 Fetch Join (N+1 방지용)
    @Query("SELECT m FROM Mission m JOIN FETCH m.place")
    List<Mission> findAllWithPlace();
}
