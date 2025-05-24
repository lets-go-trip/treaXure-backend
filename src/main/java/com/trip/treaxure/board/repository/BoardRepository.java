package com.trip.treaxure.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.place.entity.Place;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    // 미션 + 사용자 기준 단건
    Optional<Board> findByMission_MissionIdAndMemberId(Long missionId, Long memberId);

    // 사용자 전체 board
    List<Board> findAllByMemberId(Long memberId);

    // 사용자 전체 board + mission 함께 조회 (N+1 방지용)
    @Query("SELECT b FROM Board b JOIN FETCH b.mission WHERE b.memberId = :memberId")
    List<Board> findAllByMemberIdWithMission(@Param("memberId") Long memberId);

    // 전체 board 내림차순 정렬
    @Query("SELECT b FROM Board b JOIN FETCH b.mission m JOIN FETCH m.place p JOIN FETCH b.member ORDER BY b.createdAt DESC")
    List<Board> findAllWithMissionPlaceAndMember();

    // 장소별 게시물 수
    int countByMission_Place(Place place);
}
