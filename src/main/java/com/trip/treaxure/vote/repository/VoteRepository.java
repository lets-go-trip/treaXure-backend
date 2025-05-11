package com.trip.treaxure.vote.repository;

import com.trip.treaxure.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    // TODO: Add custom queries if needed
} 