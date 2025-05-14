package com.trip.treaxure.board.repository;

import com.trip.treaxure.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // TODO: Add custom queries if needed
} 