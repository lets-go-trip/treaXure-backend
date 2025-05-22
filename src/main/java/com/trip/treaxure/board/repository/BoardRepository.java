package com.trip.treaxure.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.place.entity.Place;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByMission_MissionIdAndMemberId(Long missionId, Long memberId);
    List<Board> findAllByMemberId(Long memberId);
    int countByMission_Place(Place place);
} 
