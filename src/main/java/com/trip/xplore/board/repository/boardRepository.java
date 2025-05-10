package com.trip.treaxure.board.repository;

import com.trip.treaxure.board.entity.board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface boardRepository extends JpaRepository<board, Long> {
    // TODO: Add custom queries if needed
} 