package com.trip.treaxure.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // TODO: Add custom queries if needed
} 